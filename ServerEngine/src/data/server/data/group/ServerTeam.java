package data.server.data.group;

import data.user.User;
import dto.type.out.server.DtoServerTeam;
import engine.data.Team;
import exception.server.NoSpot;
import exception.server.NotEnoughRole;
import message.UserMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class ServerTeam {
    private final Team Team;
    private final List<Role> Guessers;
    private final List<Role> Identifiers;
    private int CurrentNumGuessers;
    private int CurrentNumIdentifiers;
    private int CurrentTurnNum = 0;
    private boolean TeamReady = false;
    private final ReadWriteLock Lock = new ReentrantReadWriteLock();


    public ServerTeam(DtoServerTeam i_Data) { //todo add a check if data is right
        int numGuessers = i_Data.getNumOfGuessers();
        int numIdentifiers = i_Data.getNumOfDefiners();

        Team = new Team(i_Data.getTeam());

        if(numGuessers < 1) {
            throw new NotEnoughRole("Guessers", Team.getName());
        }
        if(numIdentifiers < 1) {
            throw new NotEnoughRole("Identifiers", Team.getName());
        }

        Guessers = new ArrayList<Role>();
        Identifiers = new ArrayList<Role>();

        for (int i = 0; i < numGuessers; i++) {
            Role toAdd = new Role(eRoles.Guesser);
            Guessers.add(toAdd);
        }

        for (int i = 0; i < numIdentifiers; i++) {
            Role toAdd = new Role(eRoles.Identifier);
            Identifiers.add(toAdd);
        }
    }


    public engine.data.Team getTeam() {
        return Team;
    }

    public int getCurrentTurnNum() {
        Lock.readLock().lock();
        try{
            return CurrentTurnNum;
        } finally {
            Lock.readLock().unlock();
        }
    }

    public int upTurn() {
        Lock.writeLock().lock();
        try{
            CurrentTurnNum++;
            return CurrentTurnNum;
        } finally {
            Lock.writeLock().unlock();
        }
    }

    public List<Role> getGuessers() {
        List<Role> ret;
        Lock.readLock().lock();
        try{
            ret = new ArrayList<>(Guessers);
        } finally {
            Lock.readLock().unlock();
        }

        return ret;
    }

    public List<Role> getIdentifiers() {
        List<Role> ret;
        Lock.readLock().lock();
        try{
            ret = new ArrayList<>(Identifiers);
        } finally {
            Lock.readLock().unlock();
        }

        return ret;
    }

    public boolean isTeamReady() {
        return TeamReady;
    }

    public int getCurrentNumGuessers() {
        return CurrentNumGuessers;
    }

    public int getCurrentNumIdentifiers() {
        return CurrentNumIdentifiers;
    }

    public void addRole(eRoles i_Role, User i_User) {
        Optional<Role> openRole = Optional.empty();
        Lock.writeLock().lock();
        try {
            boolean available = isRoleAvailable(i_Role);
            if (available) {
                UserMessage.eRole toUpdate = null;
                switch (i_Role) {
                    case Guesser:
                        openRole = Guessers.stream().filter(T -> T.getUser() == null).findFirst();
                        CurrentNumGuessers++;
                        toUpdate = UserMessage.eRole.Guesser;
                        break;
                    case Identifier:
                        openRole = Identifiers.stream().filter(T -> T.getUser() == null).findFirst();
                        CurrentNumIdentifiers++;
                        toUpdate = UserMessage.eRole.Definer;
                        break;
                }
                openRole.ifPresent(role -> role.setUser(i_User));
                i_User.setConnectedTeam(this);
                i_User.setRole(toUpdate);
            }
            else {
                if (TeamReady) {
                    throw new NoSpot(NoSpot.eNoSpot.Team);
                } else {
                    throw new NoSpot(NoSpot.eNoSpot.Role);
                }
            }

            TeamReady = CurrentNumGuessers == Guessers.size() && CurrentNumIdentifiers == Identifiers.size();
        } finally{
            Lock.writeLock().unlock();
        }
    }

    public boolean isRoleAvailable(eRoles i_Role) {
        boolean returnValue = false;

        switch(i_Role) {
            case Guesser:
                returnValue = Guessers.size() > CurrentNumGuessers;
                break;
            case Identifier:
                returnValue = Identifiers.size() > CurrentNumIdentifiers;
        }

        return returnValue;
    }

    public void removeUser(User i_User){
        Lock.writeLock().lock();
        for(Role role : Guessers){
            if(role.getUser() == i_User){
                role.setUser(null);
            }
        }
        for(Role role : Identifiers){
            if(role.getUser() == i_User){
                role.setUser(null);
            }
        }
        Lock.writeLock().unlock();
    }
}
