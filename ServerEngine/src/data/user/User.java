package data.user;


import data.server.data.ePermission;
import data.server.data.group.ServerTeam;
import message.UserMessage.eRole;



public class User {
    private final String Name;
    private int GameId; //0 = not in a game
    private int TeamId; //0 = not in a team
    private final ePermission PermissionLevel;
    private ServerTeam ConnectedTeam;
    private eRole Role;
    private int ChatUpdate = 0;
    private int TurnUpdate = 0;
    private int NextTurnId = 0;
    private int ServerUpdate = 0;
    private int BoardUpdate = 0;

    public User(String i_Name, ePermission i_PermissionLevel) {
        Name = i_Name;
        PermissionLevel = i_PermissionLevel;
        ConnectedTeam = null;
        GameId = 0;
        TeamId = 0;
    }

    public int getTeamId() {
        return TeamId;
    }

    public void setTeamId(int i_TeamId) {
        TeamId = i_TeamId;
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

    public int getChatUpdate() {
        return ChatUpdate;
    }

    public void setChatUpdate(int i_ChatUpdate) {
        ChatUpdate = i_ChatUpdate;
    }

    public void setTurnUpdate(int i_TurnUpdate) {
        TurnUpdate = i_TurnUpdate;
    }

    public void setServerUpdate(int i_ServerUpdate) {
        ServerUpdate = i_ServerUpdate;
    }

    public int getTurnUpdate() {
        return TurnUpdate;
    }

    public int getServerUpdate() {
        return ServerUpdate;
    }

    public eRole getRole() {
        return Role;
    }

    public void setRole(eRole i_Role) {
        Role = i_Role;
    }

    public int getBoardUpdate() {
        return BoardUpdate;
    }

    public void setBoardUpdate(int i_GameUpdate) {
        BoardUpdate = i_GameUpdate;
    }

    public boolean checkBoardUpdate(int i_GameUpdate) {
        return BoardUpdate == i_GameUpdate;
    }

    public boolean checkTurnUpdate(int i_TurnUpdate) {
        return TurnUpdate == i_TurnUpdate;
    }

    public boolean checkChatUpdate(int i_ChatUpdate) {
        return ChatUpdate == i_ChatUpdate;
    }

    public boolean checkServerUpdate(int i_ServerUpdate) {
        return ServerUpdate == i_ServerUpdate;
    }

    public int getNextTurnId() {
        return NextTurnId;
    }

    public void setNextTurnId(int i_NextTurnId) {
        NextTurnId = i_NextTurnId;
    }
}
