package exception.server.mismatch;

import exception.CodeNameException;

public class MismatchUpdate extends CodeNameException {
    public MismatchUpdate() {
        setType(ExceptionType.MISMATCH_UPDATE);
    }
}
