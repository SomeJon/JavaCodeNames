package message.format;

import message.format.interfaces.UserMessageFormat;

public class UserInfoMessageConsoleFormat implements UserMessageFormat {
    @Override
    public String getString(String time, String senderName, String message, String team, String role) {
        return String.format("[%s] [%s] [%s] %s: %s", time, team, role, senderName, message);
    }
}
