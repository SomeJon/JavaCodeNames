package exception.turn;

import exception.OutOfBoundException;

public class GuessOutOfRangeException extends OutOfBoundException {
    public GuessOutOfRangeException(String parameterName, int parameterValue, int max, int min) {
        super(parameterName, parameterValue, max, min);
        super.setType(ExceptionType.TURN_EXCEPTION);
    }
}
