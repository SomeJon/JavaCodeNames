package data.server.data;

import data.server.data.game.Identification;
import data.server.data.game.Turn;
import data.server.data.group.ServerTeam;
import data.server.data.group.eRoles;
import data.user.UpdateContainer;
import data.user.User;
import dto.type.in.response.ingame.IdentificationResponse;
import dto.type.out.board.DtoBoard;
import dto.type.out.board.card.DtoGroupTeam;
import dto.type.out.data.DtoActiveGameStatus;
import dto.type.out.server.DtoServerInfo;
import dto.type.out.server.DtoServerTeam;
import dto.type.out.server.game.DtoBoardUpdate;
import dto.type.out.server.game.DtoSingleTurnUpdate;
import engine.EngineInterface;
import exception.CodeNameException;
import exception.server.NoSpot;
import exception.server.Unauthorized;
import exception.server.mismatch.MismatchRole;
import exception.server.mismatch.MismatchStage;
import exception.server.mismatch.MismatchUpdate;
import exception.turn.IdentificationException;
import message.UserMessage;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SubServerData {
    private final String name;
    private final int Id;
    private Boolean Active;
    private int TurnUpdate = 0;
    private int BoardUpdate = 0;
    private int GameUpdate = 0;
    private EngineInterface Engine = null;
    private final List<ServerTeam> Teams;
    private final Map<ServerTeam, Integer> Result = new HashMap<>();
    private final List<data.server.data.game.Turn> Turns = new ArrayList<>(); //todo: remove final enable to delete for restart
    private final ReadWriteLock dataLock = new ReentrantReadWriteLock(); // Combined lock for all operations

    public SubServerData(EngineInterface engine, int id, DtoServerInfo dtoServerInfo) {
        name = dtoServerInfo.getServerName();
        Id = id;
        Engine = engine;
        Teams = new ArrayList<>();
        List<DtoServerTeam> teams = dtoServerInfo.getServerTeams();
        Active = false;

        for (DtoServerTeam team : teams) {
            ServerTeam toAdd = new ServerTeam(team);
            Teams.add(toAdd);
        }
    }

    public int getGameUpdate() {
        dataLock.readLock().lock();
        try {
            return GameUpdate;
        } finally {
            dataLock.readLock().unlock();
        }
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return Id;
    }

    public Boolean getActive() {
        dataLock.readLock().lock();
        try {
            return Active;
        } finally {
            dataLock.readLock().unlock();
        }
    }

    public int getTurnUpdate() {
        dataLock.readLock().lock();
        try {
            return TurnUpdate;
        } finally {
            dataLock.readLock().unlock();
        }
    }

    public EngineInterface getEngine() { //todo delete
        dataLock.readLock().lock();
        try {
            return Engine;
        } finally {
            dataLock.readLock().unlock();
        }
    }

    public List<DtoServerTeam> getTeams() {
        List<DtoServerTeam> teams;
        dataLock.readLock().lock();
        try {
            teams = Teams.stream()
                    .map(T ->
                            new DtoServerTeam(
                                    T.getTeam(), T.getGuessers().size(),
                                    T.getIdentifiers().size(), T.getCurrentNumGuessers(),
                                    T.getCurrentNumIdentifiers()))
                    .collect(Collectors.toList());
        } finally {
            dataLock.readLock().unlock();
        }
        return teams;
    }

    public void joinTeam(User i_User, int TeamId, int RoleChoice) {
        eRoles toAdd = (RoleChoice == 0) ? eRoles.Identifier : eRoles.Guesser;
        dataLock.writeLock().lock();
        try {
            Teams.get(TeamId - 1).addRole(toAdd, i_User);
            GameUpdate++;
            StartTry();
        } catch (NoSpot error) {
            if (Teams.stream().allMatch(ServerTeam::isTeamReady)) {
                throw new NoSpot(NoSpot.eNoSpot.Game);
            } else {
                throw error;
            }
        } finally {
            dataLock.writeLock().unlock();
        }
    }

    private void StartTry() {
        boolean check = Teams.stream().allMatch(ServerTeam::isTeamReady);
        if (check) {
            Active = true;
            dataLock.writeLock().lock();
            try {
                Engine.startGame();
                Turns.add(buildTurn());
                TurnUpdate++;
                BoardUpdate++;
                GameUpdate++;
            } finally {
                dataLock.writeLock().unlock();
            }
        }
    }

    public int getBoardUpdate() {
        int ret;
        dataLock.readLock().lock();

        try{
            ret = BoardUpdate;
        } finally {
            dataLock.readLock().unlock();
        }

        return ret;
    }

    private Turn buildTurn() {
        DtoGroupTeam team = (DtoGroupTeam) Engine.getActiveTeam();
        DtoGroupTeam nextTeam = (DtoGroupTeam) Engine.getNextTeam();
        String targetName = team.getName();
        int teamId = -1;
        OptionalInt indexOpt = IntStream.range(0, Teams.size())
                .filter(i -> Teams.get(i).getTeam().getName().equals(targetName))
                .findFirst();

        if (indexOpt.isPresent()) {
            teamId = indexOpt.getAsInt();
        }

        return new Turn(teamId + 1, Teams.get(teamId).upTurn(), team, nextTeam);
    }

    public DtoSingleTurnUpdate getTurnUpdates(UpdateContainer io_Container) {
        DtoSingleTurnUpdate delta = null;
        dataLock.readLock().lock();
        try {
            if (io_Container.checkTurnUpdate(TurnUpdate)) {
                delta = Turns.get(Turns.size() - 1).getDto();
                io_Container.setTurnUpdate(TurnUpdate);
            }
        } finally {
            dataLock.readLock().unlock();
        }
        return delta;
    }

    public DtoBoardUpdate getBoardUpdates(UpdateContainer io_Container) {
        DtoBoardUpdate ret = null;
        dataLock.readLock().lock();
        try {
            if (io_Container.checkBoardUpdate(BoardUpdate)) {
                ret = new DtoBoardUpdate((DtoBoard) Engine.getActiveBoard(), Engine.didGameEng());
                io_Container.setBoardUpdate(BoardUpdate);
            }
        } finally {
            dataLock.readLock().unlock();
        }
        return ret;
    }

    public void playIdentification(User i_User, IdentificationResponse i_Identification)
            throws MismatchUpdate, MismatchRole, MismatchStage,
            IndexOutOfBoundsException , IdentificationException {
        dataLock.writeLock().lock();
        try{
            Turn currentTurn = Turns.get(Turns.size() - 1);
            if(currentTurn.getState() == Turn.eState.IDENTIFICATION) {
                if (i_User.getUpdates().checkTurnUpdate(TurnUpdate)) {
                    if (i_User.getRole() == UserMessage.eRole.Definer) {
                        dto.type.out.data.DtoIdentification ServerSide =
                                Engine.playTurnIdentification(i_Identification);
                        Identification toAdd = new Identification(
                                ServerSide.getIdentification(), ServerSide.getRelated());
                        currentTurn.setTurnIdentification(toAdd);
                        GameUpdate++;
                        TurnUpdate++;
                        BoardUpdate++;
                    } else {
                        throw new MismatchRole();
                    }
                } else {
                    throw new MismatchUpdate();
                }
            }
            else {
                throw new MismatchStage();
            }
        } finally {
            dataLock.writeLock().unlock();
        }
    }

    public DtoActiveGameStatus getActiveGameStatus() throws Unauthorized{
        dataLock.readLock().lock();
        try{
            if(!Active)
                throw new Unauthorized();
            return Engine.getActiveGameStatus();
        } finally {
            dataLock.readLock().unlock();
        }
    }
}
