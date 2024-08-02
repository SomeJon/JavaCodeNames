package message.format;

import message.format.interfaces.SystemMessageFormat;

public class SystemMessageConsoleFormat implements SystemMessageFormat {
    @Override
    public String getString(String time, String actionType, String message) {
        return String.format("[%s] [%s] %s", time, "Game message: ", message);
    }
}
