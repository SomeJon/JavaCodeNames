package data.server.controllers;

import data.server.data.SubServerChat;
import data.server.data.SubServerData;
import dto.type.out.data.DtoGameDetails;
import dto.type.out.server.DtoServerInfo;
import dto.type.out.server.DtoSubServerStatus;
import engine.EngineInterface;

public class SubServer {
    private SubServerData Data;
    private final DtoSubServerStatus Status;
    private final SubServerChat Chat = new SubServerChat();

    public SubServer(EngineInterface engine, int id, DtoServerInfo dtoServerInfo) {
        Data = new SubServerData(engine, id, dtoServerInfo);
        DtoGameDetails details = (DtoGameDetails) engine.getStatus();
        Status = new DtoSubServerStatus(dtoServerInfo, details);
    }

    //todo: add user verification

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
}
