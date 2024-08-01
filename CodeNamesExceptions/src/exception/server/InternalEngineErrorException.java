package exception.server;

import exception.CodeNameException;

public class InternalEngineErrorException extends CodeNameException {
    public InternalEngineErrorException() {
        setType(ExceptionType.ENGINE);
    }
}
