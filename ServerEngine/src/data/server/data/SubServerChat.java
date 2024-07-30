package data.server.data;

import data.ChatData;
import data.user.User;
import dto.type.out.server.chat.DtoServerChat;
import message.Message;
import message.UserMessage;

import java.util.List;

public class SubServerChat {
    private final ChatData chatData = new ChatData();

    public void addUserMessage(User i_User, String i_Message) {
        UserMessage newMessage = new UserMessage(i_Message, i_User.getName(),
                i_User.getRole(), i_User.getConnectedTeam().getTeam().getName());

        chatData.AddMessage(newMessage);
    }

    public DtoServerChat getNewMessages(User i_User) {
        List<Message> newMessages = chatData.getMessages(i_User.getChatUpdate());
        DtoServerChat ret = null;

        if(newMessages != null){
            i_User.setChatUpdate(chatData.getCurrentUpdate());
            ret = new DtoServerChat(i_User.getChatUpdate(), newMessages);
        }

        return ret;
    }


}
