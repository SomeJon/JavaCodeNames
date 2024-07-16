package exception.server;

import exception.CodeNameException;

public class NoSpot extends CodeNameException {
    //todo add more info

    public NoSpot() {
        super.setType(ExceptionType.SERVER_NO_SPOT);
    }
}
