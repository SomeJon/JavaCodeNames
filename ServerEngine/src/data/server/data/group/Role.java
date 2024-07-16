package data.server.data.group;

import data.user.User;

public class Role {
    private User user;
    private final eRoles Role;


    public Role(eRoles role) {
        Role = role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User i_user) {
        user = i_user;
    }

    public eRoles getRole() {
        return Role;
    }
}
