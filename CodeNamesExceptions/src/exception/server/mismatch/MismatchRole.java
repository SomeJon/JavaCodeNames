package exception.server.mismatch;

import exception.CodeNameException;

public class MismatchRole extends CodeNameException{
    public MismatchRole() {
        setType(CodeNameException.ExceptionType.UNAUTHORIZED);
    }
}
