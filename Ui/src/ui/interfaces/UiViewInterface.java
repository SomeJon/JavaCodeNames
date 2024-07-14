package ui.interfaces;

import dto.type.board.DtoBoard;
import dto.type.data.DtoActiveGameStatus;
import dto.type.data.DtoGameDetails;
import dto.type.data.DtoGuessResult;
import engine.board.card.GroupTeam;
import engine.data.Identification;
import engine.exception.CodeNameException;
import engine.response.Response;
import ui.MenuAction;

public interface UiViewInterface {

    public void buildStartingMenu(); //Builds start menu
    public MenuAction openMenu();
    public void showBoard(DtoBoard ReceivedBoard, boolean i_Visible);
    public void getResponse(Response o_Response);
    public void askForXml();
    public void updateBoard(DtoBoard i_ReceivedBoard);
    public void addFileData();
    public void exceptionHandler(CodeNameException i_ReceivedError, boolean i_TryAgain);
    public void showGameDetails(DtoGameDetails i_ReceivedGameStatus);
    public void showTeam(GroupTeam i_PlayingTeam);
    public void showIdentification(Identification i_CurrentIdentification, int i_GuessesLeft);
    public void guessResult(DtoGuessResult i_ReceivedGuessResult, int i_GuessLeft, GroupTeam i_PlayingTeam);
    public void victoryHandler(GroupTeam i_WinnerTeam);
    public void showActiveGameStatus(DtoActiveGameStatus i_Data);
    public void successLoad();
}
