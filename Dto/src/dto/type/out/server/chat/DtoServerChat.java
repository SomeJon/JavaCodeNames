package dto.type.out.server.chat;

import dto.Dto;

import java.util.List;
import message.Message;

public class DtoServerChat implements Dto {
    private final int UpdateNum;
    private final List<Message> ReceivedMessages;

    public DtoServerChat(int updateNum, List<Message> receivedMessages) {
        UpdateNum = updateNum;
        ReceivedMessages = receivedMessages;
    }

    public int getUpdateNum() {
        return UpdateNum;
    }

    public List<Message> getReceivedMessages() {
        return ReceivedMessages;
    }
}
