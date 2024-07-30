package data.server.data;

import data.server.data.group.ServerTeam;
import data.server.data.group.eRoles;
import data.user.User;
import dto.type.out.server.DtoServerInfo;
import dto.type.out.server.DtoServerTeam;
import engine.EngineInterface;
import exception.server.NoSpot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class SubServerData {
    private final String name;
    private final int Id;
    private Boolean Active;
    private AtomicInteger Turn;
    private EngineInterface Engine = null;
    private final List<ServerTeam> Teams;
    private final ReadWriteLock TeamsLock = new ReentrantReadWriteLock();

    public SubServerData(EngineInterface engine, int id, DtoServerInfo dtoServerInfo) {
        name = dtoServerInfo.getServerName();
        Id = id;
        Engine = engine;
        Teams = new ArrayList<ServerTeam>();
        List<DtoServerTeam> teams = dtoServerInfo.getServerTeams();
        Turn = new AtomicInteger(0);
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

    public AtomicInteger getTurn() {
        return Turn;
    }

    public EngineInterface getEngine() {
        return Engine;
    }

    public void turnUp(){
        Turn.incrementAndGet();
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
            Turn.incrementAndGet();
            Active = true;
            Engine.startGame();
        }
    }
}
