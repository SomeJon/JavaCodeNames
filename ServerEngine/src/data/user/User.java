package data.user;


import data.server.data.ePermission;
import data.server.data.group.ServerTeam;

public class User {
    private final String Name;
    private int GameId;
    private final ePermission PermissionLevel;
    private ServerTeam ConnectedTeam;

    public User(String i_Name, ePermission i_PermissionLevel) {
        Name = i_Name;
        PermissionLevel = i_PermissionLevel;
        ConnectedTeam = null;
        GameId = 0;
    }

    public ServerTeam getConnectedTeam() {
        return ConnectedTeam;
    }

    public void setConnectedTeam(ServerTeam i_ConnectedTeam) {
        ConnectedTeam = i_ConnectedTeam;
    }

    public String getName() {
        return Name;
    }

    public int getGameId() {
        return GameId;
    }

    public void setGameId(int i_GameId) {
        GameId = i_GameId;
    }

    public Boolean isAdmin() {
        return PermissionLevel == ePermission.Admin;
    }

    public ePermission getPermissionLevel() {
        return PermissionLevel;
    }
}
