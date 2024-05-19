package engine.ui;

import engine.Engine;
import engine.board.Board;
import engine.data.GameStatus;
import engine.exception.CodeNameExceptions;
import engine.response.Response;

public interface UiAction {

    public void buildStartingMenu(); //Builds start menu
    public Engine.MenuAction openMenu();
    public void showBoard(Board ReceivedBoard, boolean i_Visible);
    public void getResponse(Response o_Response);
    public void askForXml();
    public void updateBoard(Board i_ReceivedBoard);
    public void addFileData();
    public void exceptionHandler(CodeNameExceptions i_ReceivedError);
    public void showGameDetails(GameStatus i_ReceivedGameStatus);
//    public void showTurn(Team PlayingTeam, Board RecivedBoard);
//    public Definition getDefinition();
//    public void guessResult(String Received);
}
