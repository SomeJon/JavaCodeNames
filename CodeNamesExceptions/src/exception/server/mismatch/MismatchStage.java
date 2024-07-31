package exception.server.mismatch;

import exception.CodeNameException;

public class MismatchStage extends CodeNameException {
    public MismatchStage() {
        setType(ExceptionType.MISMATCH_TURN_STAGE);
    }
}
