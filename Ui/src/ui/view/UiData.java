package ui.view;

import console.MainMenu;
import ui.interfaces.UiActionConst;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class UiData implements UiActionConst, Serializable {
    private final MainMenu MainMenu;
    private int CardLineSize;
    private int LineSize;
    private List<String> FirstLines;
    private String ClosingLine;
    private boolean LoadedAFile = false;
    private boolean ActiveGame;
    private boolean MenuBuilt = false;
    private InputHandling NextInput;


    public void flipMenuBuilt(){
        MenuBuilt = !MenuBuilt;
    }

    public boolean isMenuBuilt() {
        return MenuBuilt;
    }

    public boolean isActiveGame() {
        return ActiveGame;
    }

    public void flipActiveGame() {
        ActiveGame = !ActiveGame;
    }

    public InputHandling getNextInput() {
        return NextInput;
    }

    public void setNextInput(InputHandling i_NextInput) {
        NextInput = i_NextInput;
    }

    public String getClosingLine() {
        return ClosingLine;
    }

    public void fileLoaded(){
        LoadedAFile = true;
    }

    public boolean hasAFile() {
        return LoadedAFile;
    }

    public List<String> getFirstLines() {
        return FirstLines;
    }

    public void setFirstLines(List<String> firstLines) {
        FirstLines = firstLines;
    }

    public int getLineSize() {
        return LineSize;
    }

    public int getCardLineSize() {
        return CardLineSize;
    }

    public void setCardLineSize(int i_CardLineSize, int i_NumOfRowCards) {
        CardLineSize = i_CardLineSize + CARD_LINE_SPACING;

        String dashes = " " + String.join("", Collections.nCopies(CardLineSize, "-"));
        StringBuilder closingLine = new StringBuilder();
        LineSize = (CardLineSize + SPACE_BETWEEN_CARDS) * i_NumOfRowCards + SPACE_BETWEEN_CARDS;
        for(int i = 0; i < i_NumOfRowCards; i++){
            closingLine.append(dashes);
        }

        ClosingLine = closingLine.toString();
    }

    public MainMenu getMainMenu() {
        return MainMenu;
    }

    public UiData() {
        MainMenu = new MainMenu("Code Names: Main Menu");
    }
}
