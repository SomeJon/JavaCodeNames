package data.server.data;

import data.user.User;
import data.user.UserManager;

import java.util.ArrayList;
import java.util.List;

public class ServerData {
    private Boolean AdminOn = false;
    private int NumberOfConnectedUsers = 0;
    private final UserManager UserManager = new UserManager();


    public Boolean getAdminOn() {
        return AdminOn;
    }

    public void setAdminOn(Boolean i_AdminOn) {
        AdminOn = i_AdminOn;
    }

    public int getNumberOfConnectedUsers() {
        return NumberOfConnectedUsers;
    }

    public void setNumberOfConnectedUsers(int i_NumberOfConnectedUsers) {
        NumberOfConnectedUsers = i_NumberOfConnectedUsers;
    }

    public data.user.UserManager getUserManager() {
        return UserManager;
    }
}
