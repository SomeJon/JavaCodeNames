package exception.server.mismatch;

import exception.CodeNameException;

public class MismatchTeam extends CodeNameException {
    public MismatchTeam() {
        setType(ExceptionType.MISMATCH_TEAM);
    }
}
