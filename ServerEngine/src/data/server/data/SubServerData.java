package data.server.data;

import data.server.data.group.ServerTeam;
import dto.type.out.server.DtoServerInfo;
import dto.type.out.server.DtoServerTeam;
import engine.EngineInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SubServerData {
    private final String name;
    private final int Id;
    private Boolean Active;
    private AtomicInteger Turn;
    private EngineInterface Engine = null;
    private final List<ServerTeam> Teams;

    public SubServerData(EngineInterface engine, int id, DtoServerInfo dtoServerInfo) {
        //todo check if we need to check the file name or whatever
        name = dtoServerInfo.getServerName();
        Id = id;
        Engine = engine;
        Teams = new ArrayList<ServerTeam>();
        List<DtoServerTeam> teams = dtoServerInfo.getServerTeams();
        Turn = new AtomicInteger();
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

    public List<ServerTeam> getTeams() {
        return Teams;
    }

    public void turnUp(){
        Turn.incrementAndGet();
    }
}
