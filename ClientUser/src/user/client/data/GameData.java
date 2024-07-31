package user.client.data;

import dto.type.out.server.game.DtoBoardUpdate;
import dto.type.out.server.game.DtoGameUpdate;
import dto.type.out.server.game.DtoSingleTurnUpdate;
import prints.boardprinting.BoardPrinting;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class GameData {
    public enum roleChoice{
        IDENTIFIER{
            @Override
            public String toString() {
                return "Identifier";
            }

            @Override
            public int GetChoice() {
                return 0;
            }
        },
        GUESSER{
            @Override
            public String toString() {
                return "Guesser";
            }

            @Override
            public int GetChoice() {
                return 1;
            }
        };

        public abstract String toString();
        public abstract int GetChoice();
    }

    private String GameName = null;
    private Integer GameId = null;
    private String TeamName = null;
    private Integer TeamId = null;
    private roleChoice Role = null;
    private DtoSingleTurnUpdate CurrentTurn = null;
    private DtoBoardUpdate CurrentBoard = null;
    private BoardPrinting Printing = new BoardPrinting();
    private boolean IsTeamTurn = false;
    private boolean IsPlayerTurn = false;
    private ReadWriteLock GameLock = new ReentrantReadWriteLock();


    public String getGameName() {
        return GameName;
    }

    public void setGameName(String i_GameName) {
        GameName = i_GameName;
    }

    public Integer getGameId() {
        return GameId;
    }

    public void setGameId(Integer i_GameId) {
        GameId = i_GameId;
    }

    public String getTeamName() {
        return TeamName;
    }

    public void setTeamName(String i_TeamName) {
        TeamName = i_TeamName;
    }

    public Integer getTeamId() {
        return TeamId;
    }

    public void setTeamId(Integer i_TeamId) {
        TeamId = i_TeamId;
    }

    public roleChoice getRole() {
        return Role;
    }

    public void setRole(roleChoice i_Role) {
        Role = i_Role;
    }

    public void clear(){
        GameId = null;
        TeamId = null;
        TeamName = null;
        Role = null;
        GameName = null;
    }

    public void loadTurn(DtoSingleTurnUpdate i_Turn){
        GameLock.writeLock().lock();
        try {
            CurrentTurn = i_Turn;
            if (CurrentTurn.getPlayingTeam().getName().equalsIgnoreCase(TeamName)) {
                IsTeamTurn = true;
                switch (Role) {
                    case GUESSER:
                        IsPlayerTurn = CurrentTurn.getTurnRole().equals(DtoSingleTurnUpdate.eDtoState.GUESSING);
                        break;
                    case IDENTIFIER:
                        IsPlayerTurn = CurrentTurn.getTurnRole().equals(DtoSingleTurnUpdate.eDtoState.IDENTIFICATION);
                        break;
                }
            }
        } finally {
            GameLock.writeLock().unlock();
        }
    }

    public void loadBoard(DtoBoardUpdate i_Board){
        GameLock.writeLock().lock();
        try {
            CurrentBoard = i_Board;
        } finally {
            GameLock.writeLock().unlock();
        }
    }

    public void loadGame(DtoGameUpdate i_Update){
        loadTurn(i_Update.getTurnUpdate());
        loadBoard(i_Update.getBoardUpdate());
    }

    public boolean isPlayerTurn() {
        boolean ret;
        GameLock.readLock().lock();
        try{
            ret = IsPlayerTurn;
        } finally {
            GameLock.readLock().unlock();
        }

        return ret;
    }

    public boolean isTeamTurn() {
        boolean ret;
        GameLock.readLock().lock();
        try{
            ret = IsTeamTurn;
        } finally {
            GameLock.readLock().unlock();
        }

        return ret;
    }

    public DtoSingleTurnUpdate getCurrentTurn() {
        DtoSingleTurnUpdate ret;
        GameLock.readLock().lock();
        try{
            ret = CurrentTurn;
        } finally {
            GameLock.readLock().unlock();
        }

        return ret;
    }

    public void printBoard(){
        boolean visible = Role.GetChoice() == 0;
        GameLock.readLock().lock();
        try{
            Printing.parse(CurrentBoard.getBoard(), visible);
        } finally {
            GameLock.readLock().unlock();
        }
    }

}
