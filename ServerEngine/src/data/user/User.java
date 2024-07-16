package data.user;


import data.server.data.ePermission;

public class User {
    private boolean JoinedGame = false;
    private final String Name;
    private int GameId = 0;
    private final ePermission PermissionLevel;

    public User(String i_Name, ePermission i_PermissionLevel) {
        Name = i_Name;
        PermissionLevel = i_PermissionLevel;
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

    public boolean isJoinedGame() {
        return JoinedGame;
    }

    public void setJoinedGame(boolean i_JoinedGame) {
        JoinedGame = i_JoinedGame;
    }

    public ePermission getPermissionLevel() {
        return PermissionLevel;
    }
}
