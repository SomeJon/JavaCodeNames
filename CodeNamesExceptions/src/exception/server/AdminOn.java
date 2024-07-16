package exception.server;

import exception.CodeNameException;

public class AdminOn extends CodeNameException {
    public AdminOn() {
        super.setType(ExceptionType.SERVER_ADMIN_ON);
    }
}
