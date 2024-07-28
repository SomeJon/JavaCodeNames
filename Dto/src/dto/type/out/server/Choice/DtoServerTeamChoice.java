package dto.type.out.server.Choice;

import dto.type.out.server.DtoServerTeam;

public class DtoServerTeamChoice {
    private final int TeamId;
    private final DtoServerTeam TeamInfo;

    public DtoServerTeamChoice(int teamId, DtoServerTeam teamInfo) {
        TeamId = teamId;
        TeamInfo = teamInfo;
    }

    public int getTeamId() {
        return TeamId;
    }

    public DtoServerTeam getTeamInfo() {
        return TeamInfo;
    }
}
