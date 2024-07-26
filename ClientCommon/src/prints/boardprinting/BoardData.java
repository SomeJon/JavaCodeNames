package prints.boardprinting;

import prints.boardprinting.interfaces.BoardConst;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BoardData implements BoardConst {
    private List<String> FirstLines = new ArrayList<String>();
    private int CardLineSize;
    private int LineSize;
    private String ClosingLine;
    private boolean ActiveGame = false;

    public boolean isActiveGame() {
        return ActiveGame;
    }

    public void setActiveGame(boolean i_ActiveGame) {
        ActiveGame = i_ActiveGame;
    }

    public List<String> getFirstLines() {
        return FirstLines;
    }

    public void setFirstLines(List<String> i_FirstLines) {
        FirstLines = i_FirstLines;
    }

    public int getCardLineSize() {
        return CardLineSize;
    }

    public int getLineSize() {
        return LineSize;
    }

    public String getClosingLine() {
        return ClosingLine;
    }

    public void setClosingLine(String i_ClosingLine) {
        ClosingLine = i_ClosingLine;
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
}
