package exception.turn;

import exception.OutOfBoundException;

public class IdentificationException extends OutOfBoundException {
    @Override
    public ExceptionType getType() {
        return super.getType();
    }

    @Override
    public void setType(ExceptionType i_Type) {
        super.setType(i_Type);
    }

    public IdentificationException(String parameterName, int parameterValue, int max, int min) {
        super(parameterName, parameterValue, max, min);
        super.setType(ExceptionType.TURN_EXCEPTION);
    }
}
