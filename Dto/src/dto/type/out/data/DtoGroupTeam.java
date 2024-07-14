package dto.type.out.data;

import dto.Dto;
import engine.board.card.GroupTeam;

public class DtoGroupTeam implements Dto{
    private final GroupTeam PlayingTeam;

    public GroupTeam getPlayingTeam() {
        return PlayingTeam;
    }

    public DtoGroupTeam(GroupTeam playingTeam) {
        PlayingTeam = (GroupTeam) playingTeam.getCopy();
    }
}
