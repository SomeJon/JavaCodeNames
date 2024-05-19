package engine;

import engine.data.GameData;
import engine.exception.CodeNameExceptions;
import engine.response.LoadXmlResponse;
import engine.response.Response;
import ui.UiAction;
import jaxb.schema.FileReader;

import java.io.File;
import java.io.FileNotFoundException;

public class Engine {
    public enum MenuAction {
        LOAD_XML,
        SHOW_GAME_DATA,
        START_GAME,
        PLAYER_TURN,
        GAME_STATUS,
        CLOSE
    }
    public enum eGuessResult{
        CorrectTeam,
        WrongTeam,
        BlackWord,
        NeutralWord
    }


    private final GameData Data;
    private final UiAction Ui;

    public Engine(UiAction i_UiLink) {
        Data = new GameData(i_UiLink);
        Ui = i_UiLink;
    }

    public void startGame(){
        MenuAction menuAction;
        Ui.buildStartingMenu();

        do {
            menuAction = Ui.openMenu();

            switch (menuAction) {
                case LOAD_XML:
                    loadXml();
                    break;
                case GAME_STATUS:
                    Ui.showGameDetails(Data.getStatus());
                    break;
                case START_GAME:
                    Data.startBoard();
                    Ui.updateBoard(Data.getActiveData().getPlayingBoard());
                    Ui.showBoard(Data.getActiveData().getPlayingBoard(), false);
                    Ui.showBoard(Data.getActiveData().getPlayingBoard(), true);
                    break;
            }

        }while(menuAction != MenuAction.CLOSE);
    }

    private void loadXml() {
        Ui.askForXml();
        LoadXmlResponse loadXml = new LoadXmlResponse();
        Ui.getResponse(loadXml);
        if(loadXml.receivedResponse()) {
            File responseFile = loadXml.getInputFile();
            try {
                FileReader.ReadXml(responseFile, Data);
                Ui.addFileData();
            } catch (CodeNameExceptions e) {
                System.out.println("Error reading xml file");
            } catch (Exception ignore) {}
        }
    }
}
