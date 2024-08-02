package data.server.data;

import data.ChatData;
import data.user.UpdateContainer;
import data.user.User;
import dto.type.out.server.chat.DtoServerChat;
import message.Message;
import message.SystemMessage;
import message.UserMessage;

import java.util.ArrayList;
import java.util.List;

public class SubServerChat extends ChatData {
    private final ChatData chatData = new ChatData();

    public void addUserMessage(User i_User, String i_Message) {
        UserMessage newMessage = new UserMessage(i_Message, i_User.getName(),
                i_User.getRole(), i_User.getConnectedTeam().getTeam().getName());

        AddMessage(newMessage);
    }

    public void addSystemMessage(String i_Message) {
        SystemMessage newMessage = new SystemMessage(i_Message, SystemMessage.eType.Result);

        chatData.AddMessage(newMessage);
    }

    public DtoServerChat getNewMessages(UpdateContainer io_Container) {
        DtoServerChat ret = null;

        lock.readLock().lock();
        try{
            int from = io_Container.getChatUpdate();
            if (Messages.size() >= from) {
                List<Message> newMessages = new ArrayList<>(Messages.subList(from, Messages.size()));
                io_Container.setChatUpdate(Messages.size());
                ret = new DtoServerChat(Messages.size(), newMessages);
            }
        }finally{
            lock.readLock().unlock();
        }

        return ret;
    }


}
