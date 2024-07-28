package message;

import message.format.interfaces.Format;

public abstract class Message {
    protected final String Message;
    protected long TimeStamp;

    public Message(String message) {
        Message = message;
    }

    public abstract String getMessage(Format format);

    public void setTimeStamp() {
        TimeStamp = System.currentTimeMillis();
    }
}
