package dto.type.out.board.card;

import dto.type.out.data.DtoTeam;
import engine.board.card.GroupCard;
import engine.board.card.GroupTeam;

public class DtoGroupTeam extends DtoGroupCard{
    private final DtoTeam Team;

    public DtoGroupTeam(int cards, int cardsFlipped, DtoTeam team) {
        super(cards, cardsFlipped);
        Team = team;
    }

    public DtoGroupTeam(GroupCard groupCard, DtoTeam team) {
        super(groupCard);
        Team = team;
    }

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
