package ui;

import dto.Dto;
import dto.type.out.board.DtoBoard;
import dto.type.out.board.card.DtoGroupTeam;
import dto.type.out.data.DtoGameDetails;
import dto.type.out.data.DtoGameEndResult;
import dto.type.out.data.DtoGuessResult;
import engine.EngineInterface;
import dto.type.out.data.DtoIdentification;
import exception.CodeNameException;
import dto.type.in.response.ingame.GuesserResponse;
import dto.type.in.response.ingame.IdentificationResponse;
import dto.type.in.response.load.LoadXmlResponse;
import dto.type.in.response.Response;
import ui.interfaces.UiViewInterface;
import ui.save.FileLocationResponse;
import ui.save.SaveObject;
import ui.view.UiView;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Controller{
    public static int EndGuessId = 0;
    private UiViewInterface Ui;
    private EngineInterface Engine;


    public void Start(){
        MenuAction menuAction;
        Ui.buildStartingMenu();

        do {
            menuAction = Ui.openMenu();

            switch (menuAction) {
                case LOAD_XML:
                    loadXml();
                    break;
                case SHOW_GAME_DATA:
                    showGameData();
                    break;
                case START_GAME:
                    startGameInstance();
                    break;
                case PLAYER_TURN:
                    playTurn();
                    break;
                case GAME_STATUS:
                    gameStatus();
                    break;
                case SAVE_GAME_DATA:
                    try {
                        saveGame();
                    }
                    catch (IOException e){
                        UiView.errorPrint("Error while trying to save file");
                    }
                    break;
                case LOAD_GAME_DATA:
                    loadGame();
                    break;

            }

        }while(menuAction != MenuAction.CLOSE);
    }

    public Controller(UiViewInterface view, EngineInterface engine) {
        Ui = view;
        Engine = engine;
    }

    private void loadXml() {
        Ui.askForXml();
        Response xmlResponse = new LoadXmlResponse();
        Ui.getResponse(xmlResponse);

        if(xmlResponse.receivedResponse()){
            try {
                Engine.loadFiles(xmlResponse);
                Ui.addFileData();
            } catch (CodeNameException e) {
                Ui.exceptionHandler(e, false);
            } catch (Exception ignore) {}
        }
    }

    private void showGameData(){
        DtoGameDetails status = (DtoGameDetails)Engine.getStatus();
        Ui.showGameDetails(status);
    }

    private void startGameInstance(){
        Engine.startGame();
        DtoBoard Board = (DtoBoard)Engine.getActiveBoard();
        Ui.updateBoard(Board);
        Ui.showBoard((DtoBoard)Engine.getActiveBoard(), false);
    }

    private void playTurn(){
        IdentificationResponse response;
        boolean loopContinue;
        DtoIdentification currentIdentification = null;
        DtoGroupTeam playingTeam = (DtoGroupTeam)Engine.getActiveTeam();
        DtoBoard playingBoard = (DtoBoard)Engine.getActiveBoard();

        Ui.showTeam(playingTeam);
        Ui.showBoard(playingBoard, true);

        do {
            response = new IdentificationResponse();
            Ui.getResponse(response);
            try{
                currentIdentification = Engine.playTurnIdentification(response);
                loopContinue = false;
            }
            catch(CodeNameException e){
                Ui.exceptionHandler(e, false);
                loopContinue = true;
            }
        }while(loopContinue);

        playGuesserTurn(currentIdentification);
    }

    private void playGuesserTurn(DtoIdentification i_Identification){
        boolean loopContinue = false;
        GuesserResponse response;
        DtoGroupTeam playingTeam = ((DtoGroupTeam)Engine.getActiveTeam());
        Dto engineResult;
        DtoGuessResult guessResult;
        boolean gameEnded = false;
        int guessCount = i_Identification.getRelated();

        Ui.showTeam(playingTeam);
        Ui.showBoard((DtoBoard)Engine.getActiveBoard(), false);
        do{
            response = new GuesserResponse();
            Ui.showIdentification(i_Identification, guessCount);
            Ui.getResponse(response);

            if(response.getCardId() == EndGuessId){
                loopContinue = false;
                Ui.showTeam(((DtoGroupTeam) Engine.getActiveTeam()));
            }
            else {
                try {
                    engineResult = Engine.playTurnGuessers(response);
                    if (engineResult instanceof DtoGameEndResult) {
                        guessResult = ((DtoGameEndResult) engineResult).getGuessResult();
                        gameEnded = true;
                    } else {
                        guessResult = (DtoGuessResult) engineResult;
                        engineResult = null;
                    }
                    playingTeam = ((DtoGroupTeam)Engine.getActiveTeam());
                    loopContinue = guessResultHandler(guessResult, (DtoGameEndResult) engineResult,
                            guessCount, playingTeam);
                    guessCount--;
                    if (loopContinue && guessCount > 0) {
                        Ui.showBoard((DtoBoard) Engine.getActiveBoard(), false);
                    }
                } catch (CodeNameException e) {
                    Ui.exceptionHandler(e, true);
                    loopContinue = true;
                }
            }
        }while(loopContinue && !gameEnded && (guessCount > 0));

        Engine.nextTeam();
    }

    private boolean guessResultHandler(DtoGuessResult i_GuessResult, DtoGameEndResult i_EndResult,
                                       int i_GuessLeft, DtoGroupTeam i_CurrentTeam){
        boolean gameEnded = i_EndResult != null;
        boolean loopContinue = false;

        Ui.guessResult(i_GuessResult, i_GuessLeft - 1, i_CurrentTeam);
        if (gameEnded) {
                    Ui.victoryHandler((i_EndResult).getWinningTeam());
        }
        else if(i_GuessResult != DtoGuessResult.BLACK_HIT && i_GuessLeft > 1) {
                loopContinue = true;
                Ui.showTeam(((DtoGroupTeam) Engine.getActiveTeam()));
        }

        return loopContinue;
    }

    private void gameStatus(){
        Ui.showActiveGameStatus(Engine.getActiveGameStatus());
    }

    private void saveGame() throws IOException {
        FileLocationResponse response = new FileLocationResponse();
        Ui.getResponse(response);

        if(!response.receivedResponse()){
            UiView.errorPrint("No file path was given!");
        }
        else{
            String fileName = response.getFileLocation();
            Path path = Paths.get(fileName);
            SaveObject toSave = new SaveObject(Ui, Engine);
            try (ObjectOutputStream out =
                new ObjectOutputStream(
                        Files.newOutputStream(path))) {
                out.writeObject(toSave);
                out.flush();
            }
        }
    }

    private void loadGame(){
        FileLocationResponse response = new FileLocationResponse();
        SaveObject load;
        Ui.getResponse(response);

        if(!response.receivedResponse()){
            UiView.errorPrint("No file path was given!");
        } else {
            String fileName = response.getFileLocation();
            Path path = Paths.get(fileName);

            if(Files.exists(path)) {
                if (!path.endsWith(".xml")) {
                    UiView.errorPrint("File is of incorrect type!");
                }
                try (ObjectInputStream in =
                             new ObjectInputStream(
                                     Files.newInputStream(path))) {
                    load = (SaveObject) in.readObject();
                    Engine = load.getEngine();
                    Ui = load.getUi();
                } catch (IOException | ClassNotFoundException e) {
                    UiView.errorPrint("File is corrupted");
                }
            }
            else{
                UiView.errorPrint("No file was found");
            }
        }
    }
}
