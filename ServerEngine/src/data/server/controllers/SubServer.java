package data.server.controllers;

import data.server.data.SubServerChat;
import data.server.data.SubServerData;
import dto.type.out.data.DtoGameDetails;
import dto.type.out.server.DtoServerInfo;
import dto.type.out.server.DtoServerTeam;
import dto.type.out.server.DtoSubServerStatus;
import engine.EngineInterface;

import java.util.List;

public class SubServer {
    private final SubServerData Data;
    private final DtoSubServerStatus Status;
    private final SubServerChat Chat = new SubServerChat();

    public SubServer(EngineInterface engine, int id, DtoServerInfo dtoServerInfo) {
        Data = new SubServerData(engine, id, dtoServerInfo);
        DtoGameDetails details = (DtoGameDetails) engine.getStatus();
        Status = new DtoSubServerStatus(dtoServerInfo, details);
    }

    public SubServerData getData() {
        return Data;
    }

    public String getServerName() {
        return Data.getName();
    }

    public DtoSubServerStatus getStatus() {
        Status.setActive(Data.getActive());
        Status.setServerTeams(Data.getTeams());
        return Status;
    }

    public List<DtoServerTeam> getServerTeams() {
        return Data.getTeams();
    }

    public boolean getActiveState(){
        return Data.getActive();
    }

    public int getUpdate(){
        return Data.getBoardUpdate();
    }
}
