package message;

import message.format.interfaces.Format;
import message.format.interfaces.SystemMessageFormat;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SystemMessage extends message.Message {
    public enum eType{
        Guess,
        Define,
        Result;

        public String toString() {
            String ret = null;
            switch(this) {
                case Guess:
                    ret = "Guess";
                    break;
                case Define:
                    ret = "Define";
                    break;
                case Result:
                    ret = "Result";
                    break;
            }

            return ret;
        }
    }

    private final eType Type;

    public SystemMessage(String i_Message, eType i_Type) {
        super(i_Message);
        Type = i_Type;
    }

    @Override
    public String getMessage(Format format) {
        SystemMessageFormat formatter = (SystemMessageFormat)format;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");
        Date date = new Date(TimeStamp);
        String time = simpleDateFormat.format(date);

        return formatter.getString(time, Type.toString(), Message);
    }
}
