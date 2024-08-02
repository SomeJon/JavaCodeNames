package data;

import dto.type.out.server.chat.DtoServerChat;
import message.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ChatData {
    protected List<Message> Messages = new ArrayList<>();
    protected ReadWriteLock lock = new ReentrantReadWriteLock();

    public int AddMessage(Message m) {
        lock.writeLock().lock();
        try {
            m.setTimeStamp();
            Messages.add(m);
            return Messages.size();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public int AddMessages(List<Message> m) {
        lock.writeLock().lock();
        try{
            Messages.addAll(m);
            return Messages.size();
        }finally {
            lock.writeLock().unlock();
        }
    }

    public List<Message> getUpdatedMessages(int i_LastUpdate) {
        List<Message> ret = null;

        lock.readLock().lock();
        try {
            if (Messages.size() >= i_LastUpdate) {
                ret = new ArrayList<>(Messages.subList(i_LastUpdate, Messages.size()));
            }
        } finally {
            lock.readLock().unlock();
        }

        return ret;
    }

    public List<Message> getMessages() {
        List<Message> ret = null;

        lock.readLock().lock();
        try {
            return Messages;
        } finally {
            lock.readLock().unlock();
        }
    }

    public DtoServerChat toDto(){
        lock.readLock().lock();
        try{
            return new DtoServerChat(Messages.size(), Messages);
        }finally {
            lock.readLock().unlock();
        }
    }

    public void clean(){
        lock.writeLock().lock();
        try{
            Messages = new ArrayList<>();
        } finally{
            lock.writeLock().unlock();
        }
    }
}
