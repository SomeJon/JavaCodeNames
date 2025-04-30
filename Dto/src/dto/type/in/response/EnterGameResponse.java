package dto.type.in.response;

import dto.type.in.roles.DtoRole;

public class EnterGameResponse implements Response{
    private Integer GameId = null;
    private Integer TeamId = null;
    private DtoRole Role = null;

    public EnterGameResponse(int gameId, int teamId, DtoRole role) {
        GameId = gameId;
        TeamId = teamId;
        Role = role;
    }

    public EnterGameResponse() {
    }

    public int getGameId() {
        return GameId;
    }

    public int getTeamId() {
        return TeamId;
    }

    public DtoRole getRole() {
        return Role;
    }

    @Override
    public void loadResponse(Response i_Response) {
        EnterGameResponse response = (EnterGameResponse)i_Response;
        GameId = response.getGameId();
        TeamId = response.getTeamId();
        Role = response.getRole();
    }

    @Override
    public boolean receivedResponse() {
        return GameId != null && TeamId != null && Role != null;
    }
}
