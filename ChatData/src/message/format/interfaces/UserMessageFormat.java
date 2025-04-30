package message.format.interfaces;

public interface UserMessageFormat extends Format {
    String getString(String time, String senderName, String message, String team, String role);
}
