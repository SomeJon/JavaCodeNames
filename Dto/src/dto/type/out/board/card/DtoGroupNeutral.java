package dto.type.out.board.card;

import dto.Dto;
import engine.board.card.GroupCard;
import engine.board.card.GroupNeutral;

public class DtoGroupNeutral extends DtoGroupCard{
    private final boolean IsBlack;

    public DtoGroupNeutral(GroupNeutral groupNeutral) {
        super(groupNeutral);
        IsBlack = groupNeutral.isBlack();
    }

    public boolean isBlack() {
        return IsBlack;
    }
}
