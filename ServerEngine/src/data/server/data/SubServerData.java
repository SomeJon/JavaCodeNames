package data.server.data;

import data.server.data.game.Guess;
import data.server.data.game.Identification;
import data.server.data.game.Turn;
import data.server.data.group.ServerTeam;
import data.server.data.group.eRoles;
import data.user.UpdateContainer;
import data.user.User;
import dto.Dto;
import dto.type.in.response.ingame.GuesserResponse;
import dto.type.in.response.ingame.IdentificationResponse;
import dto.type.out.board.DtoBoard;
import dto.type.out.board.card.DtoGroupTeam;
import dto.type.out.data.DtoActiveGameStatus;
import dto.type.out.data.DtoGameEndResult;
import dto.type.out.data.DtoGuessResult;
import dto.type.out.data.DtoGuessResultWrapper;
import dto.type.out.server.DtoServerInfo;
import dto.type.out.server.DtoServerTeam;
import dto.type.out.server.game.DtoBoardUpdate;
import dto.type.out.server.game.DtoEndResult;
import dto.type.out.server.game.DtoSingleTurnUpdate;
import engine.EngineInterface;
import engine.board.card.GroupTeam;
import exception.server.InternalEngineErrorException;
import exception.server.NoSpot;
import exception.server.Unauthorized;
import exception.server.mismatch.MismatchRole;
import exception.server.mismatch.MismatchStage;
import exception.server.mismatch.MismatchTeam;
import exception.server.mismatch.MismatchUpdate;
import exception.turn.CardFlippedException;
import exception.turn.GuessOutOfRangeException;
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
    private List<data.server.data.game.Turn> Turns = new ArrayList<>();
    private final ReadWriteLock dataLock = new ReentrantReadWriteLock();
    private int WinPlacement = 1;

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

    private Turn buildTurn() throws IndexOutOfBoundsException{
        DtoGroupTeam team = (DtoGroupTeam) Engine.getActiveTeam();
        DtoGroupTeam nextTeam = (DtoGroupTeam) Engine.getNextTeam();
        String targetName = team.getName();
        int teamId = -1;
        OptionalInt indexOpt = IntStream.range(0, Teams.size())
                .filter(i -> Teams.get(i).getTeam().getName().equals(targetName))
                .findFirst();

        ServerTeam turnTeam;
        if (indexOpt.isPresent()) {
            teamId = indexOpt.getAsInt();
            turnTeam = Teams.get(teamId);
        } else{
            throw new IndexOutOfBoundsException();
        }

        return new Turn(turnTeam, teamId + 1, Teams.get(teamId).upTurn(), team, nextTeam);
    }

    public DtoSingleTurnUpdate getTurnUpdates(UpdateContainer io_Container) {
        DtoSingleTurnUpdate delta = null;
        dataLock.readLock().lock();
        try {
            if (io_Container.checkTurnUpdate(GameUpdate)) {
                delta = Turns.get(Turns.size() - 1).getDto();
                io_Container.setTurnUpdate(GameUpdate);
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
                ret = new DtoBoardUpdate((DtoBoard) Engine.getActiveBoard(), Engine.didGameEnd());
                io_Container.setBoardUpdate(BoardUpdate);
            }
        } finally {
            dataLock.readLock().unlock();
        }
        return ret;
    }

    public void playIdentification(User i_User, IdentificationResponse i_Identification)
            throws MismatchUpdate, MismatchRole, MismatchStage, MismatchTeam,
            IndexOutOfBoundsException , IdentificationException {
        dataLock.writeLock().lock();
        try {
            Turn currentTurn = getCurrentTurn();
            validateTurn(currentTurn, i_User, Turn.eState.IDENTIFICATION, UserMessage.eRole.Definer);

            dto.type.out.data.DtoIdentification ServerSide =
                    Engine.playTurnIdentification(i_Identification);
            Identification toAdd = new Identification(
                    ServerSide.getIdentification(), ServerSide.getRelated());
            currentTurn.setTurnIdentification(toAdd);
            currentTurn.setState(Turn.eState.GUESSING);
            GameUpdate++;
            TurnUpdate++;

        } finally {
            dataLock.writeLock().unlock();
        }
    }

    public DtoGuessResultWrapper playGuesser(User i_User, GuesserResponse i_Guess)
            throws MismatchUpdate, MismatchRole, MismatchStage, MismatchTeam,
            IndexOutOfBoundsException, InternalEngineErrorException,
            GuessOutOfRangeException, CardFlippedException {
        dataLock.writeLock().lock();
        try {
            Turn currentTurn = getCurrentTurn();
            validateTurn(currentTurn, i_User, Turn.eState.GUESSING, UserMessage.eRole.Guesser);
            boolean turnEnd = false;
            boolean gameEnd = false;
            DtoGuessResult guessResult;
            Guess toAdd;

            if (i_Guess.getCardId() == 0) {
                guessResult = DtoGuessResult.TURN_SKIPPED;
                toAdd = new Guess(0, Guess.eResult.SKIP);
                turnEnd = true;
            } else {

                Dto ServerSide = Engine.playTurnGuessers(i_Guess);
                if (ServerSide instanceof DtoGuessResult) {
                    guessResult = (DtoGuessResult) ServerSide;
                    toAdd = new Guess(i_Guess.getCardId(), guessResult);
                    if (guessResult == DtoGuessResult.BLACK_HIT) {
                        DtoEndResult endResult = new DtoEndResult(guessResult, 0, false);
                        currentTurn.getTurnTeam().cleanTeam(endResult);
                        turnEnd = true;
                    }
                } else if (ServerSide instanceof DtoGameEndResult) {
                    DtoGameEndResult gameEndResult = (DtoGameEndResult) ServerSide;
                    DtoEndResult endResult;
                    DtoEndResult winResult;
                    ServerTeam winningTeam;
                    guessResult = gameEndResult.getGuessResult();
                    toAdd = new Guess(i_Guess.getCardId(), guessResult);
                    gameEnd = Engine.didGameEnd();

                    switch (guessResult) {
                        case BLACK_HIT:
                            endResult = new DtoEndResult(guessResult, 0, gameEnd);
                            currentTurn.getTurnTeam().cleanTeam(endResult);
                            winResult = new DtoEndResult(guessResult, WinPlacement, gameEnd);
                            WinPlacement++;
                            winningTeam = getTeam(gameEndResult.getWinningTeam());
                            winningTeam.cleanTeam(winResult);
                            turnEnd = true;
                            break;
                        case SUCCESSFUL_GUESS:
                            winResult = new DtoEndResult(guessResult, WinPlacement, gameEnd);
                            WinPlacement++;
                            currentTurn.getTurnTeam().cleanTeam(winResult);
                            turnEnd = true;
                            if (gameEnd) {
                                endResult = new DtoEndResult(guessResult, 0, gameEnd);
                                winningTeam = getTeam(((DtoGroupTeam) Engine.getActiveTeam()));
                                winningTeam.cleanTeam(endResult);
                            }
                            break;
                        case ENEMY_TEAM_HIT:
                            winResult = new DtoEndResult(guessResult, WinPlacement, gameEnd);
                            WinPlacement++;
                            winningTeam = getTeam(gameEndResult.getWinningTeam());
                            winningTeam.cleanTeam(winResult);
                            if (gameEnd) {
                                endResult = new DtoEndResult(guessResult, 0, gameEnd);
                                currentTurn.getTurnTeam().cleanTeam(endResult);
                            }
                            break;
                    }
                } else {
                    throw new InternalEngineErrorException();
                }
            }
            currentTurn.guessDone();
            if (currentTurn.getGuessesLeft() < 1)
                turnEnd = true;
            currentTurn.addGuess(toAdd);

            if (turnEnd) {
                if (currentTurn.getPlayingTeam().getName()
                        .equalsIgnoreCase
                                (((DtoGroupTeam) Engine.getActiveTeam()).getName())) {
                    Engine.nextTeam();
                }

                Turns.add(buildTurn());
            } else {
                currentTurn.setPlayingTeam((DtoGroupTeam) Engine.getActiveTeam());
                currentTurn.setNextPlayingTeam((DtoGroupTeam) Engine.getNextTeam());
            }

            GameUpdate++;
            TurnUpdate++;
            BoardUpdate++;
            if (gameEnd) {
                cleanGame();
            }
            return new DtoGuessResultWrapper(guessResult);

        } finally {
            dataLock.writeLock().unlock();
        }
    }

    private Turn getCurrentTurn() throws IndexOutOfBoundsException {
        return Turns.get(Turns.size() - 1);
    }

    private void validateTurn(Turn currentTurn, User user,
                              Turn.eState wantedState, UserMessage.eRole wantedRole)
        throws MismatchTeam, MismatchStage, MismatchUpdate, MismatchRole {
        if (currentTurn.getTeamId() != user.getTeamId()) {
            throw new MismatchTeam();
        }
        if (currentTurn.getState() != wantedState) {
            throw new MismatchStage();
        }
        if (!user.getUpdates().checkTurnUpdate(TurnUpdate)) {
            throw new MismatchUpdate();
        }
        if (user.getRole() != wantedRole) {
            throw new MismatchRole();
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

    private void removeTeam(ServerTeam i_Team, DtoEndResult i_Result){
        i_Team.cleanTeam(i_Result);
    }

    private ServerTeam getTeam(DtoGroupTeam i_Team){
        Optional<ServerTeam> teamRet = Teams.stream().filter(T -> T.getTeam().getName().equalsIgnoreCase(i_Team.getName())).findFirst();
        ServerTeam ret = null;

        if(teamRet.isPresent()){
            ret = teamRet.get();
        }

        return ret;
    }

    private void cleanGame(){
        DtoEndResult endResult = new DtoEndResult
                (DtoGuessResult.ENEMY_TEAM_HIT, 0, true);

        for(ServerTeam i_Team : Teams){
            i_Team.cleanTeam(endResult);
        }

        Active = false;
        TurnUpdate = 0;
        BoardUpdate = 0;
        GameUpdate = 0;
        Turns = new ArrayList<>();
        WinPlacement = 1;
    }
}
