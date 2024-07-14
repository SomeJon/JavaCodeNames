package dto.type.out.data;

import dto.Dto;

public enum DtoGuessResult implements Dto {
    SUCCESSFUL_GUESS,
    ENEMY_TEAM_HIT,
    NEUTRAL_HIT,
    BLACK_HIT;

    private DtoGroupTeam GroupTeam;

    public DtoGroupTeam getGroupTeam() {
        return GroupTeam;
    }

    public void setGroupTeam(DtoGroupTeam i_playingTeam) {
        GroupTeam = i_playingTeam;
    }
}
