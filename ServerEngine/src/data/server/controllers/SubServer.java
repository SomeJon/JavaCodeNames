package data.server.controllers;

import data.server.data.SubServerData;
import dto.type.out.server.DtoServerInfo;
import engine.EngineInterface;

public class SubServer {
    private SubServerData ServerData;

    public SubServer(EngineInterface engine, int id, DtoServerInfo dtoServerInfo) {
        ServerData = new SubServerData(engine, id, dtoServerInfo);
    }

    //todo: add user verification

    public SubServerData getServerData() {
        return ServerData;
    }

    public String getServerName() {
        return ServerData.getName();
    }
}
