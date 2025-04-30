package exception.server;

import exception.CodeNameException;

public class BadExtension extends CodeNameException {
    private final String Expected;
    private final String Received;

    public BadExtension(String expected, String recieved) {
        Expected = expected;
        Received = recieved;
    }

    public String getExpected() {
        return Expected;
    }

    public String getReceived() {
        return Received;
    }
}
