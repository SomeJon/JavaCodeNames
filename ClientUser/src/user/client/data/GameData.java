package user.client.data;

import dto.type.in.response.Response;
import dto.type.out.server.game.DtoBoardUpdate;
import dto.type.out.server.game.DtoGameUpdate;
import dto.type.out.server.game.DtoSingleTurnUpdate;
import prints.boardprinting.BoardPrinting;
import ui.input.InputHandling;

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

            @Override
            public void getInput(Response o_Response) {
                InputHandling.IDENTIFICATION.getInput(o_Response);
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

            @Override
            public void getInput(Response o_Response) {
                InputHandling.GUESSER.getInput(o_Response);
            }
        };

        public abstract String toString();
        public abstract int GetChoice();
        public abstract void getInput(Response o_Response);
    }

    private String GameName = null;
    private Integer GameId = 0;
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
        if(i_Turn != null) {
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
    }

    public void loadBoard(DtoBoardUpdate i_Board){
        if(i_Board != null) {
            GameLock.writeLock().lock();
            try {
                CurrentBoard = i_Board;
            } finally {
                GameLock.writeLock().unlock();
            }
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

    public String getParsedBoard(){
        boolean visible = Role.GetChoice() == 0;
        GameLock.readLock().lock();
        try{
            return Printing.parse(CurrentBoard.getBoard(), visible);
        } finally {
            GameLock.readLock().unlock();
        }
    }

}
