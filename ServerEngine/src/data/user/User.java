package data.user;


import data.server.data.ePermission;
import data.server.data.group.ServerTeam;
import dto.type.out.server.game.DtoEndResult;
import message.UserMessage.eRole;


public class User {
    private final String Name;
    private int GameId; //0 = not in a game
    private int TeamId; //0 = not in a team
    private final ePermission PermissionLevel;
    private ServerTeam ConnectedTeam;
    private eRole Role;
    private UpdateContainer Updates;
    private DtoEndResult EndResult = new DtoEndResult();

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

    public eRole getRole() {
        return Role;
    }

    public void setRole(eRole i_Role) {
        Role = i_Role;
    }

    public UpdateContainer getUpdates() {
        return Updates;
    }

    public void setUpdates(UpdateContainer i_Updates) {
        Updates = i_Updates;
    }

    public DtoEndResult getEndResult() {
        DtoEndResult ret = EndResult;
        EndResult = new DtoEndResult();
        return ret;
    }

    public void setEndResult(DtoEndResult i_EndResult) {
        EndResult = i_EndResult;
    }
}
