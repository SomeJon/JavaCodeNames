package data.user;

import data.server.data.ePermission;
import exception.server.AdminOn;
import exception.server.NameTaken;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class UserManager {
    private final Set<User> Users;
    private final ReadWriteLock Lock = new ReentrantReadWriteLock();

    public UserManager() {
        this.Users = new HashSet<User>();
    }

    public Set<User> getUsers() {
        return Collections.unmodifiableSet(Users);
    }

    public User addNormalUser(String i_Username) {
        User user;

        Lock.writeLock().lock();
        try {
            boolean found = Users.stream().anyMatch(T -> T.getName().equalsIgnoreCase(i_Username));
            if(!found) {
                user = new User(i_Username, ePermission.Common);
                Users.add(user);
            }
            else{
                throw new NameTaken(i_Username.toUpperCase());
            }
        }
        finally {
            Lock.writeLock().unlock();
        }

        return user;
    }

    public User addAdmin() throws AdminOn{
        User user;

        Lock.writeLock().lock();
        try {
            boolean check = Users.stream().anyMatch(User::isAdmin);
            if (!check) {
                user = new User("", ePermission.Admin);
                Users.add(user);
            } else {
                throw new AdminOn();
            }
        }
        finally {
            Lock.writeLock().unlock();
        }

        return user;
    }

    public void removeUser(User user) {
        Lock.writeLock().lock();
        Users.remove(user);
        Lock.writeLock().unlock();
    }

    public Boolean isAdminOn() {
        boolean ret;

        Lock.readLock().lock();
        ret = Users.stream().anyMatch(User::isAdmin);
        Lock.readLock().unlock();

        return ret;
    }

    public boolean isUserOn(String i_Username) {
        boolean ret;

        Lock.readLock().lock();
        ret = Users.stream().anyMatch(user -> user.getName().equalsIgnoreCase(i_Username));
        Lock.readLock().unlock();

        return ret;
    }
}
