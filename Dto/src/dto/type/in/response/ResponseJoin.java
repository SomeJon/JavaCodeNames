package dto.type.in.response;

import dto.Dto;

public class ResponseJoin implements Response {
    private Integer GameId;
    private Integer TeamId;
    private Integer RoleChoice;

    public ResponseJoin(Integer gameId, Integer teamId, Integer roleChoice) {
        GameId = gameId;
        TeamId = teamId;
        RoleChoice = roleChoice;
    }

    public ResponseJoin() {
        GameId = null;
        TeamId = null;
        RoleChoice = null;
    }

    public Integer getGameId() {
        return GameId;
    }

    public Integer getTeamId() {
        return TeamId;
    }

    public Integer getRoleChoice() {
        return RoleChoice;
    }

    @Override
    public void loadResponse(Response i_Response) {
        ResponseJoin responseJoin = (ResponseJoin) i_Response;
        GameId = responseJoin.getGameId();
        TeamId = responseJoin.getTeamId();
        RoleChoice = responseJoin.getRoleChoice();
    }

    @Override
    public boolean receivedResponse() {
        return GameId != null && TeamId != null && RoleChoice != null;
    }
}
