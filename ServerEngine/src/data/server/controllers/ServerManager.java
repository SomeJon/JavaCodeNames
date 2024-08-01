package data.server.controllers;

import data.server.data.ServerData;
import data.server.data.ePermission;
import data.user.UpdateContainer;
import data.user.User;
import dto.type.in.response.ingame.GuesserResponse;
import dto.type.in.response.ingame.IdentificationResponse;
import dto.type.in.response.load.LoadInputStreamsResponse;
import dto.type.out.data.DtoActiveGameStatus;
import dto.type.out.data.DtoGuessResult;
import dto.type.out.server.Choice.DtoServerGameChoice;
import dto.type.out.server.Choice.DtoServerTeamChoice;
import dto.type.out.server.Choice.DtoSubServerChoice;
import dto.type.out.server.DtoServerInfo;
import dto.type.out.server.DtoServerStatus;
import dto.type.out.server.DtoSubServerStatus;
import dto.type.out.server.game.DtoBoardUpdate;
import dto.type.out.server.game.DtoGameUpdate;
import dto.type.out.server.game.DtoSingleTurnUpdate;
import engine.Engine;
import engine.data.GameData;
import exception.server.AdminOn;
import exception.server.InternalEngineErrorException;
import exception.server.NameTaken;
import exception.server.Unauthorized;
import exception.server.mismatch.MismatchRole;
import exception.server.mismatch.MismatchStage;
import exception.server.mismatch.MismatchTeam;
import exception.server.mismatch.MismatchUpdate;
import exception.turn.CardFlippedException;
import exception.turn.GuessOutOfRangeException;
import exception.turn.IdentificationException;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ServerManager {
    private final ServerData Data = new ServerData();
    private final ReadWriteLock SubServersLock = new ReentrantReadWriteLock();
    private final List<SubServer> subServers = new ArrayList<>();
    private boolean hasGame = false;
    private ReadWriteLock UpdateLock = new ReentrantReadWriteLock();

    public boolean hasGame() {
        return hasGame;
    }

    public void joinGame(User i_User, int GameId, int TeamId, int RoleChoice){
        UpdateLock.writeLock().lock();
        try {
            subServers.get(GameId - 1).getData().joinTeam(i_User, TeamId, RoleChoice);
            Data.updateCount();
            i_User.setGameId(GameId);
            i_User.setTeamId(TeamId);
        } finally {
            UpdateLock.writeLock().unlock();
        }
    }

    /**
     * Admin entry method to add an admin user.
     *
     * @return User object representing the admin user.
     * @throws AdminOn if an admin is already present.
     */
    public User adminEntry() throws AdminOn {
        return Data.getUserManager().addAdmin();
    }

    /**
     * Check if an admin is already present.
     *
     * @return true if an admin is present, false otherwise.
     */
    public boolean isAdminOn() {
        return Data.getUserManager().isAdminOn();
    }

    /**
     * User entry method to add a normal user.
     *
     * @param i_UserName Name of the user to be added.
     * @return User object representing the added user.
     */
    public User userEntry(String i_UserName){
        return Data.getUserManager().addNormalUser(i_UserName);
    }

    /**
     * Remove a user.
     *
     * @param i_User User object representing the user to be removed.
     * @return true if the user was removed successfully, false otherwise.
     */
    public boolean removeUser(User i_User){
        return Data.getUserManager().removeUser(i_User);
    }

    /**
     * Load sub-server data from input streams.
     *
     * @param i_Response LoadInputStreamsResponse object containing input streams and server info.
     * @throws JAXBException if an error occurs during XML processing.
     * @throws IOException if an I/O error occurs.
     */
    public void loadSubServerData(LoadInputStreamsResponse i_Response) throws JAXBException, IOException {
        Engine toAdd = new Engine(new GameData());
        toAdd.loadFiles(i_Response);

        SubServersLock.writeLock().lock();
        try {
            DtoServerInfo dtoServerInfo = i_Response.getDtoToLoad();
            boolean checkName = subServers.stream().noneMatch(T -> T.getServerName()
                    .equalsIgnoreCase(dtoServerInfo.getServerName()));
            if (checkName) {
                SubServer newSubServer = new SubServer(toAdd, subServers.size() + 1, dtoServerInfo);
                UpdateLock.writeLock().lock();
                try {
                    subServers.add(newSubServer);
                    hasGame = true;
                    Data.updateCount();
                }
                finally {
                    UpdateLock.writeLock().unlock();
                }
            }
            else{
                throw new NameTaken(dtoServerInfo.getServerName());
            }
        }
        finally {
            SubServersLock.writeLock().unlock();
        }
    }

    /**
     * Check if a user has permission to access a specific server.
     *
     * @param i_User User object representing the user.
     * @param i_ServerId ID of the server.
     * @return true if the user has permission, false otherwise.
     */
    private boolean checkPermission(User i_User, int i_ServerId) {
        boolean ret;

        if(i_User.getPermissionLevel() == ePermission.Admin)
            ret = true;
        else ret = i_User.getConnectedTeam() != null && i_User.getGameId() == i_ServerId;

        return ret;
    }

    /**
     * Get the number of sub-server states.
     *
     * @return Number of sub-servers.
     */
    public int numberOfSubServerState() {
        SubServersLock.readLock().lock();
        try {
            return subServers.size();
        } finally {
            SubServersLock.readLock().unlock();
        }
    }

    /**
     * Get the status of all servers.
     *
     * @return DtoServerStatus object containing the status of all servers.
     */
    public DtoServerStatus getServerStatus() {
        SubServersLock.readLock().lock();
        try {
            List<DtoSubServerStatus> statuses = subServers.stream()
                .map(SubServer::getStatus)
                .collect(Collectors.toList());
            return new DtoServerStatus(statuses);
        } finally {
            SubServersLock.readLock().unlock();
        }
    }

    /**
     * Get game choices from all sub-servers.
     *
     * @return DtoServerGameChoice object containing game choices from all sub-servers.
     */
    public DtoServerGameChoice getServerGameChoices(Integer o_Update) {
        SubServersLock.readLock().lock();
        try {
            List<DtoSubServerChoice> choices = IntStream.range(0, subServers.size())
                .mapToObj(i -> {
                    SubServer subServer = subServers.get(i);
                    List<DtoServerTeamChoice> teamsToAdd = IntStream.range(0, subServer.getServerTeams().size())
                        .mapToObj(j -> new DtoServerTeamChoice(j + 1, subServer.getServerTeams().get(j)))
                        .collect(Collectors.toList());
                    return new DtoSubServerChoice(i + 1, subServer.getActiveState(), subServer.getServerName(), teamsToAdd);
                })
                .collect(Collectors.toList());

            return new DtoServerGameChoice(choices);
        } finally {
            o_Update = Data.getUpdateCount();
            SubServersLock.readLock().unlock();
        }
    }

    public int getUpdateCount(){
        int ret;
        UpdateLock.readLock().lock();
        try{
            ret = Data.getUpdateCount();
        } finally {
            UpdateLock.readLock().unlock();
        }

        return ret;
    }

    public boolean didGameStart(int GameId){
        return subServers.get(GameId - 1).getData().getActive();
    }

    public DtoSubServerChoice getGameChoice(int GameId, UpdateContainer i_Container) {
        SubServersLock.readLock().lock();
        try {
            SubServer subServer = subServers.get(GameId - 1);
            DtoSubServerChoice choice;
            int update = subServer.getData().getGameUpdate();
            if(i_Container.checkGameUpdate(update)) {
                List<DtoServerTeamChoice> teamsToAdd = IntStream.range(0, subServer.getServerTeams().size())
                        .mapToObj(j -> new DtoServerTeamChoice(j + 1, subServer.getServerTeams().get(j)))
                        .collect(Collectors.toList());
                choice = new DtoSubServerChoice(GameId, subServer.getActiveState(),
                        subServer.getServerName(), teamsToAdd);
                i_Container.setGameUpdate(update);
            } else{
                choice = null;
            }
            return choice;
        } finally {
            SubServersLock.readLock().unlock();
        }
    }

    public DtoActiveGameStatus getActiveGameStatus(int GameId) throws Unauthorized, IndexOutOfBoundsException{
        SubServersLock.readLock().lock();
        try{
            return subServers.get(GameId - 1).getActiveGameStatus();
        } finally {
            SubServersLock.readLock().unlock();
        }
    }

    public DtoGameUpdate getUpdates(User i_User) throws Unauthorized{
        int gameId = i_User.getGameId();
        if(gameId == 0)
            throw new Unauthorized();
        SubServersLock.readLock().lock();
        try {
            DtoBoardUpdate dtoBoard = subServers.get(gameId - 1).getData().getBoardUpdates(i_User.getUpdates());
            DtoSingleTurnUpdate dtoTurns = subServers.get(gameId - 1).getData().getTurnUpdates(i_User.getUpdates());

            return new DtoGameUpdate(dtoBoard, dtoTurns);
        } finally {
            SubServersLock.readLock().unlock();
        }
    }

    private SubServer getSubServer(int gameId){
        return subServers.get(gameId - 1);
    }

    public void playIdentification(User i_User, IdentificationResponse i_Identification)
            throws MismatchUpdate, MismatchRole, MismatchStage, MismatchTeam,
            IndexOutOfBoundsException , IdentificationException {
        SubServersLock.writeLock().lock();
        try {
            SubServer subServer = getSubServer(i_User.getGameId() - 1);
            subServer.playIdentification(i_User, i_Identification);
        } finally{
            SubServersLock.writeLock().unlock();
        }
    }

    public DtoGuessResult playGuess(User i_User, GuesserResponse i_Guess)
        throws MismatchUpdate, MismatchRole, MismatchStage, MismatchTeam,
            IndexOutOfBoundsException, InternalEngineErrorException,
            GuessOutOfRangeException, CardFlippedException {
        SubServersLock.writeLock().lock();
        try {
            return getSubServer(i_User.getGameId() - 1).playGuess(i_User, i_Guess);
        } finally{
            SubServersLock.writeLock().unlock();
        }
    }
}
