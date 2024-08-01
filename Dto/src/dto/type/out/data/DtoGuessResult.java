package dto.type.out.data;

import dto.Dto;
import dto.type.out.board.card.DtoGroupTeam;

public enum DtoGuessResult implements Dto {
    SUCCESSFUL_GUESS,
    ENEMY_TEAM_HIT,
    NEUTRAL_HIT,
    BLACK_HIT,
    GAME_NOT_END,
    TURN_SKIPPED;

    private DtoGroupTeam GroupTeam;

    public DtoGroupTeam getGroupTeam() {
        return GroupTeam;
    }

    public void setGroupTeam(DtoGroupTeam i_playingTeam) {
        GroupTeam = i_playingTeam;
    }

    DtoGuessResult(DtoGroupTeam groupTeam) {
        GroupTeam = groupTeam;
    }

    DtoGuessResult() {
    }
}
