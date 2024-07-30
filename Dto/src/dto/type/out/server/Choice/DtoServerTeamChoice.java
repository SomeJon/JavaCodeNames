package dto.type.out.server.Choice;

import dto.Dto;
import dto.type.out.server.DtoServerTeam;

public class DtoServerTeamChoice implements Dto {
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
