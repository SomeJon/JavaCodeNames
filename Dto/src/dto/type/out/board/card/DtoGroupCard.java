package dto.type.out.board.card;

import dto.Dto;
import engine.board.card.GroupCard;
import engine.board.card.GroupNeutral;
import engine.board.card.GroupTeam;

public abstract class DtoGroupCard implements Dto {
    protected final int Cards;
    protected final int CardsFlipped;

    public DtoGroupCard(GroupCard groupCard) {
        Cards = groupCard.getCards();
        CardsFlipped = groupCard.getCardsFlipped();
    }

    public int getCards() {
        return Cards;
    }

    public int getCardsFlipped() {
        return CardsFlipped;
    }

    public static DtoGroupCard getGroupCard(GroupCard groupCard){
        if(groupCard instanceof GroupTeam){
            return new DtoGroupTeam((GroupTeam)groupCard);
        }
        if(groupCard instanceof GroupNeutral){
            return new DtoGroupNeutral((GroupNeutral)groupCard);
        }

        return null;
    }
}
