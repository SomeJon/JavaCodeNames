package dto.type.out.board.card;

import dto.type.out.data.DtoTeam;
import engine.board.card.GroupCard;
import engine.board.card.GroupNeutral;
import engine.board.card.GroupTeam;

public class DtoGroupTeam extends DtoGroupCard{
    private final DtoTeam Team;

    public DtoGroupTeam(GroupTeam groupCard) {
        super(groupCard);
        this.Team = new DtoTeam(groupCard.getTeam());
    }

    public DtoTeam getTeam() {
        return Team;
    }

    public String getName(){
        return Team.getName();
    }
}
