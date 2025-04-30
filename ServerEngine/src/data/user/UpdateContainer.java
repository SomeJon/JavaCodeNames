package data.user;

public class UpdateContainer {
    public int ChatUpdate = 0;
    public int TurnUpdate = 0;
    public int ServerUpdate = 0;
    public int BoardUpdate = 0;
    public int GameUpdate = 0;

    public void clear(){
        ChatUpdate = 0;
        TurnUpdate = 0;
        BoardUpdate = 0;
        GameUpdate = 0;
    }

    public int getChatUpdate() {
        return ChatUpdate;
    }

    public void setChatUpdate(int i_ChatUpdate) {
        ChatUpdate = i_ChatUpdate;
    }

    public int getTurnUpdate() {
        return TurnUpdate;
    }

    public void setTurnUpdate(int i_TurnUpdate) {
        TurnUpdate = i_TurnUpdate;
    }

    public int getServerUpdate() {
        return ServerUpdate;
    }

    public void setServerUpdate(int i_ServerUpdate) {
        ServerUpdate = i_ServerUpdate;
    }

    public int getBoardUpdate() {
        return BoardUpdate;
    }

    public void setBoardUpdate(int i_BoardUpdate) {
        BoardUpdate = i_BoardUpdate;
    }

    public int getGameUpdate() {
        return GameUpdate;
    }

    public void setGameUpdate(int i_GameUpdate) {
        GameUpdate = i_GameUpdate;
    }

    public boolean checkChatUpdate(int i_ChatUpdate) {
        return ChatUpdate != i_ChatUpdate;
    }

    public boolean checkTurnUpdate(int i_TurnUpdate) {
        return TurnUpdate != i_TurnUpdate;
    }

    public boolean checkServerUpdate(int i_ServerUpdate) {
        return ServerUpdate != i_ServerUpdate;
    }

    public boolean checkBoardUpdate(int i_BoardUpdate) {
        return BoardUpdate != i_BoardUpdate;
    }

    public boolean checkGameUpdate(int i_GameUpdate) {
        return GameUpdate != i_GameUpdate;
    }
}
