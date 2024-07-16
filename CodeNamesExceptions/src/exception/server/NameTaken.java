package exception.server;

import exception.CodeNameException;

public class NameTaken extends CodeNameException {
    private final String Name;

    public NameTaken(String i_Name) {
        super.setType(ExceptionType.SERVER_NAME_TAKEN);
        Name = i_Name;
    }

    public String getName() {
        return Name;
    }
}
