package data.server.controllers;

import data.server.data.ServerData;
import data.server.data.ePermission;
import data.user.User;
import dto.type.in.response.LoadInputStreamsResponse;
import dto.type.out.server.DtoServerInfo;
import dto.type.out.server.DtoServerStatus;
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
import java.util.stream.Collectors;

public class ServerManager {
    private final ServerData Data = new ServerData();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final List<SubServer> subServers = new ArrayList<>();
    private boolean hasGame = false;

    public boolean hasGame() {
        return hasGame;
    }

    public User adminEntry() throws AdminOn {
        return Data.getUserManager().addAdmin();
    }

    public boolean isAdminOn() {
        return Data.getUserManager().isAdminOn();
    }

    public User userEntry(String i_UserName){
        return Data.getUserManager().addNormalUser(i_UserName);
    }

    public boolean removeUser(User i_User){
        return Data.getUserManager().removeUser(i_User);
    }

    public void loadSubServerData(LoadInputStreamsResponse i_Response) throws JAXBException, IOException {
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
                hasGame = true;
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
        else ret = i_User.getConnectedTeam() != null && i_User.getGameId() == i_ServerId;

        return ret;
    }

    public int numberOfSubServerState(){
        return subServers.size();
    }

    public DtoServerStatus getServerStatus(){
        lock.readLock().lock();
        DtoServerStatus ret = new DtoServerStatus(subServers.stream().map(SubServer::getStatus).collect(Collectors.toList()));
        lock.readLock().unlock();

        return ret;
    }
}
