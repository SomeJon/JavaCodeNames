package exception.loadxml;

import exception.OutOfBoundException;

public class OutOfBoundLoad extends OutOfBoundException {

    public OutOfBoundLoad(String parameterName, int parameterValue, int max, int min) {
        super(parameterName, parameterValue, max, min);
        super.setType(ExceptionType.LOAD_FILE);
    }
}
