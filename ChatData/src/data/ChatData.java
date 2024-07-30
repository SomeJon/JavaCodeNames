package data;

import message.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ChatData {
    private int CurrentUpdate = 0;
    private List<Message> Messages = new ArrayList<>();
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public void AddMessage(Message m) {
        lock.writeLock().lock();
        m.setTimeStamp();
        Messages.add(m);
        CurrentUpdate++;
        lock.writeLock().unlock();
    }

    public List<Message> getMessages(int i_LastUpdate) {
        List<Message> ret = null;

        lock.readLock().lock();
        if(CurrentUpdate > i_LastUpdate) {
            ret = new ArrayList<>(Messages.subList(i_LastUpdate, Messages.size()));
        }
        lock.readLock().unlock();

        return ret;
    }

    public int getCurrentUpdate() {
        return CurrentUpdate;
    }
}
