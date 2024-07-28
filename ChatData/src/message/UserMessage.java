package message;

import message.format.interfaces.Format;
import message.format.interfaces.UserMessageFormat;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserMessage extends message.Message {
    public enum eRole{
        Definer,
        Guesser;

        public String toString() {
            String ret = null;
            switch(this) {
                case Definer:
                    ret = "Definer";
                    break;
                case Guesser:
                    ret = "Guesser";
                    break;
            }

            return ret;
        }
    }

    private final String SenderName;
    private final eRole Role;
    private final String Team;


    public UserMessage(String i_Message, String i_SenderName, eRole i_Role, String i_Team) {
        super(i_Message);
        SenderName = i_SenderName;
        Role = i_Role;
        Team = i_Team;
    }


    @Override
    public String getMessage(Format format) {
        UserMessageFormat formatter = (UserMessageFormat) format;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");
        Date date = new Date(TimeStamp);
        String time = simpleDateFormat.format(date);

        return formatter.getString(time, SenderName, Message, Team, Role.toString());
    }
}
