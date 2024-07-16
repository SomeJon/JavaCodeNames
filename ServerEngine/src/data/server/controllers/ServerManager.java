package data.server.controllers;

import data.server.data.ServerData;
import data.server.data.ePermission;
import data.user.User;
import dto.type.in.response.LoadFilesResponse;
import dto.type.out.server.DtoServerInfo;
import engine.Engine;
import engine.data.GameData;
import exception.server.AdminOn;
import exception.server.NameTaken;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ServerManager {
    private final ServerData Data = new ServerData();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final List<SubServer> subServers = new ArrayList<>();

    public User adminEntry() throws AdminOn {
        return Data.getUserManager().addAdmin();
    }

    public boolean isAdminOn() {
        return Data.getUserManager().isAdminOn();
    }

    public User userEntry(String i_UserName){
        return Data.getUserManager().addNormalUser(i_UserName);
    }

    public void loadSubServerData(LoadFilesResponse i_Response) throws JAXBException, IOException {
        Engine toAdd = new Engine(new GameData());
        toAdd.loadFiles(i_Response);

        lock.writeLock().lock();
        try {
            DtoServerInfo dtoServerInfo = i_Response.getDtoToLoad();
            boolean checkName = subServers.stream().noneMatch(T -> T.getServerName()
                    .equalsIgnoreCase(dtoServerInfo.getServerName()));
            if (checkName) {
                SubServer newSubServer = new SubServer(toAdd, subServers.size() + 1, dtoServerInfo);
                subServers.add(newSubServer);
            }
            else{
                throw new NameTaken(dtoServerInfo.getServerName());
            }
        }
        finally {
            lock.writeLock().unlock();
        }
    }

    private boolean checkPermission(User i_User, int i_ServerId) {
        boolean ret;

        if(i_User.getPermissionLevel() == ePermission.Admin)
            ret = true;
        else if(i_User.isJoinedGame() && i_User.getGameId() == i_ServerId)
            ret = true;
        else
            ret = false;

        return ret;
    }

    public int numberOfSubServerState(){
        return subServers.size();
    }
}
