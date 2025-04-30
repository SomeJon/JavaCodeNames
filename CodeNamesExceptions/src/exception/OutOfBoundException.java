package exception;

public class OutOfBoundException extends CodeNameException {
    private final String ParameterName;
    private final int ParameterValue;
    private final int Max;
    private final int Min;

    public String getParameterName() {
        return ParameterName;
    }

    public int getParameterValue() {
        return ParameterValue;
    }

    public int getMax() {
        return Max;
    }

    public int getMin() {
        return Min;
    }

    public OutOfBoundException(String parameterName, int parameterValue, int max, int min) {
        ParameterName = parameterName;
        ParameterValue = parameterValue;
        Max = max;
        Min = min;
    }
}
