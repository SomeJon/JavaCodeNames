package exception.server;

import exception.CodeNameException;

public class Unauthorized extends CodeNameException {
    public Unauthorized() {
        setType(ExceptionType.UNAUTHORIZED);
    }
}
