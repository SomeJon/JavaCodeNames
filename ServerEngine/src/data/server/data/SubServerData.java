package data.server.data;

import data.server.data.game.Turn;
import data.server.data.group.ServerTeam;
import data.server.data.group.eRoles;
import data.user.UpdateContainer;
import data.user.User;
import dto.type.out.board.DtoBoard;
import dto.type.out.board.card.DtoGroupTeam;
import dto.type.out.server.DtoServerInfo;
import dto.type.out.server.DtoServerTeam;
import dto.type.out.server.game.DtoBoardUpdate;
import dto.type.out.server.game.DtoSingleTurnUpdate;
import engine.EngineInterface;
import exception.server.NoSpot;

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
    private EngineInterface Engine = null;
    private final List<ServerTeam> Teams;
    private final Map<ServerTeam, Integer> Result = new HashMap<>();
    private final List<data.server.data.game.Turn> Turns = new ArrayList<>(); //todo: remove final enable to delete for restart
    private final ReadWriteLock TeamsLock = new ReentrantReadWriteLock(); //locks the team for reading and writing into them
    private final ReadWriteLock TurnLock = new ReentrantReadWriteLock(); //locks Turn from changing while updating or reading
    private final ReadWriteLock BoardLock = new ReentrantReadWriteLock(); //locks board from changing while board read


    public SubServerData(EngineInterface engine, int id, DtoServerInfo dtoServerInfo) {
        name = dtoServerInfo.getServerName();
        Id = id;
        Engine = engine;
        Teams = new ArrayList<ServerTeam>();
        List<DtoServerTeam> teams = dtoServerInfo.getServerTeams();
        Active = false;

        for (DtoServerTeam team : teams) {
            ServerTeam toAdd = new ServerTeam(team);
            Teams.add(toAdd);
        }
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return Id;
    }

    public Boolean getActive() {
        return Active;
    }

    public int getTurnUpdate() {
        return TurnUpdate;
    }

    public EngineInterface getEngine() {
        return Engine;
    }

    public List<DtoServerTeam> getTeams() {
        List<DtoServerTeam> teams = new ArrayList<>();

        TeamsLock.readLock().lock();
        try {
            teams = Teams.stream()
                    .map(T ->
                            new DtoServerTeam(
                                    T.getTeam(), T.getGuessers().size(),
                                    T.getIdentifiers().size(), T.getCurrentNumGuessers(),
                                    T.getCurrentNumIdentifiers()))
                    .collect(Collectors.toList());
        } finally {
            TeamsLock.readLock().unlock();
        }

        return teams;
    }

    public void joinTeam(User i_User, int TeamId, int RoleChoice) {
        eRoles toAdd;
        if (RoleChoice == 0) {
            toAdd = eRoles.Identifier;
        } else {
            toAdd = eRoles.Guesser;
        }

        TeamsLock.writeLock().lock();
        try {
            Teams.get(TeamId - 1).addRole(toAdd, i_User);
            BoardUpdate++;
            StartTry();
        }
        catch(NoSpot error){
            if(Teams.stream().allMatch(ServerTeam::isTeamReady)){
                throw new NoSpot(NoSpot.eNoSpot.Game);
            }
            else{
                throw error;
            }
        } finally {
            TeamsLock.writeLock().unlock();
        }
    }

    private void StartTry(){
        boolean check = Teams.stream().allMatch(ServerTeam::isTeamReady);
        if (check) {
            Active = true;
            BoardLock.writeLock().lock();
            TurnLock.writeLock().lock();
            TurnUpdate++;
            BoardUpdate++;
            try {
                Engine.startGame();
                Turns.add(buildTurn());
            }finally {
                TurnLock.writeLock().unlock();
                BoardLock.writeLock().unlock();
            }
        }
    }

    public int getBoardUpdate() {
        return BoardUpdate;
    }

    private Turn buildTurn(){
        DtoGroupTeam team = (DtoGroupTeam)Engine.getActiveTeam();
        DtoGroupTeam nextTeam = (DtoGroupTeam)Engine.getNextTeam();
        String targetName = team.getName();
        int teamId = -1;
        OptionalInt indexOpt = IntStream.range(0, Teams.size())
                .filter(i -> Teams.get(i).getTeam().getName().equals(targetName))
                .findFirst();

        TurnUpdate++;
        if (indexOpt.isPresent()) {
            teamId = indexOpt.getAsInt();
        }

        return new Turn(teamId + 1, Teams.get(teamId).upTurn(), team, nextTeam);
    }

    public DtoSingleTurnUpdate getTurnUpdates(UpdateContainer io_Container){
        DtoSingleTurnUpdate delta = null;
        TurnLock.readLock();
        try{
            if(io_Container.checkTurnUpdate(TurnUpdate)) {
                delta = Turns.get(Turns.size() - 1).getDto();
                io_Container.setTurnUpdate(TurnUpdate);
            }
        } finally {
            TurnLock.readLock().unlock();
        }

        return delta;
    }

    public DtoBoardUpdate getBoardUpdates(UpdateContainer io_Container){
        DtoBoardUpdate ret = null;
        BoardLock.readLock().lock();
        try{
            if(io_Container.checkBoardUpdate(BoardUpdate)) {
                ret = new DtoBoardUpdate((DtoBoard)Engine.getActiveBoard(), Engine.didGameEng());
                io_Container.setBoardUpdate(BoardUpdate);
            }
        } finally {
            BoardLock.readLock().unlock();
        }

        return ret;
    }
}
