package dto.type.out.board.card;

import dto.Dto;
import engine.board.card.Card;
import engine.board.card.GroupNeutral;
import engine.board.card.GroupTeam;

public class DtoCard implements Dto {
    private final boolean Flipped;
    private final String Text;
    private final Integer ID;
    private final DtoGroupCard Group;

    public DtoCard(Card card) {
        this.Flipped = card.isFlipped();
        this.Text = card.getText();
        this.ID = card.getID();
        if(card.getGroup() instanceof GroupTeam) {
            this.Group = new DtoGroupTeam((GroupTeam) card.getGroup());
        }
        else
            this.Group = new DtoGroupNeutral((GroupNeutral) card.getGroup());

    }

    public boolean isFlipped() {
        return Flipped;
    }

    public String getText() {
        return Text;
    }

    public Integer getID() {
        return ID;
    }

    public DtoGroupCard getGroup() {
        return Group;
    }
}
