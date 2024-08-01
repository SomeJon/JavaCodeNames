package data.server.controllers;

import data.server.data.SubServerChat;
import data.server.data.SubServerData;
import data.user.User;
import dto.type.in.response.ingame.IdentificationResponse;
import dto.type.out.data.DtoActiveGameStatus;
import dto.type.out.data.DtoGameDetails;
import dto.type.out.server.DtoServerInfo;
import dto.type.out.server.DtoServerTeam;
import dto.type.out.server.DtoSubServerStatus;
import engine.EngineInterface;
import exception.server.Unauthorized;
import exception.server.mismatch.MismatchRole;
import exception.server.mismatch.MismatchStage;
import exception.server.mismatch.MismatchUpdate;
import exception.turn.IdentificationException;

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
        return Data.getGameUpdate();
    }

    public void playIdentification(User i_User, IdentificationResponse i_Identification)
            throws MismatchUpdate, MismatchRole, MismatchStage,
            IndexOutOfBoundsException , IdentificationException {
        Data.playIdentification(i_User, i_Identification);
    }

    public DtoActiveGameStatus getActiveGameStatus() throws Unauthorized {
        return Data.getActiveGameStatus();
    }
}
