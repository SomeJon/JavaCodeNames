package message.format.interfaces;

public interface SystemMessageFormat extends Format {
    String getString(String time, String actionType, String message);
}
