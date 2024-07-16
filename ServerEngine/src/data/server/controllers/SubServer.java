package data.server.controllers;

import data.server.data.SubServerData;
import data.server.data.group.Role;
import data.server.data.group.ServerTeam;
import data.user.User;
import dto.type.out.server.DtoServerInfo;
import engine.EngineInterface;

import java.util.List;

public class SubServer {
    private SubServerData Data;

    public SubServer(EngineInterface engine, int id, DtoServerInfo dtoServerInfo) {
        Data = new SubServerData(engine, id, dtoServerInfo);
    }

    //todo: add user verification

    public SubServerData getData() {
        return Data;
    }

    public String getServerName() {
        return Data.getName();
    }
}
