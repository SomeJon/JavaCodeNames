package exception.server;

import exception.CodeNameException;

public class NoSpot extends CodeNameException {
    public enum eNoSpot{
        Role,
        Team,
        Game
    }
    private final eNoSpot Case;

    public NoSpot(eNoSpot Case) {
        super.setType(ExceptionType.SERVER_NO_SPOT);
        this.Case = Case;
    }

    public eNoSpot getCase() {
        return Case;
    }
}
