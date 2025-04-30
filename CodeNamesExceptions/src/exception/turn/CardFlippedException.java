package exception.turn;

import exception.CodeNameException;

public class CardFlippedException extends CodeNameException {
    public CardFlippedException() {
        super.setType(ExceptionType.CARD_FLIPPED);
    }
}
