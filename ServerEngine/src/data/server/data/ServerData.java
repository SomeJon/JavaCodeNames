package data.server.data;

import data.user.User;
import data.user.UserManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ServerData {
    private Boolean AdminOn = false;
    private int NumberOfConnectedUsers = 0;
    private final UserManager UserManager = new UserManager();
    private AtomicInteger UpdateCount = new AtomicInteger(0);
    private ReadWriteLock UpdateLock = new ReentrantReadWriteLock();


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

    public int getUpdateCount() {
        int ret;

        UpdateLock.readLock().lock();
        try{
            ret = UpdateCount.get();
        } finally {
            UpdateLock.readLock().unlock();
        }

        return ret;
    }

    public void updateCount() {
        UpdateLock.writeLock().lock();
        try{
            UpdateCount.incrementAndGet();
        } finally {
            UpdateLock.writeLock().unlock();
        }
    }
}
