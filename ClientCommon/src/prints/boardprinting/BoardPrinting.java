package prints.boardprinting;

import prints.boardprinting.interfaces.BoardConst;
import dto.type.out.board.DtoBoard;
import dto.type.out.board.card.DtoCard;
import dto.type.out.board.card.DtoGroupCard;
import dto.type.out.board.card.DtoGroupNeutral;
import dto.type.out.board.card.DtoGroupTeam;

import java.util.*;

public class BoardPrinting implements BoardConst {
    private final BoardData Data = new BoardData();

    public String parse(DtoBoard i_ReceivedBoard, boolean i_Visible) {
        DtoCard[][] board = i_ReceivedBoard.getBoard();
        int rows = i_ReceivedBoard.getNumOfRows();
        List<String> secondLines;
        StringBuilder builder = new StringBuilder();

        if(!Data.isActiveGame()){
            updateBoard(i_ReceivedBoard);
        }

        secondLines = createLines(board, rows, false, i_Visible);
        for(int i = 0; i < rows ; i++){
            builder.append(Data.getClosingLine()).append("\n")
                    .append(Data.getFirstLines().get(i)).append("\n")
                    .append(secondLines.get(i)).append("\n");
        }

        builder.append(Data.getClosingLine()).append("\n");

        return builder.toString();
    }

    public void updateBoard(DtoBoard i_ReceivedBoard) {
        DtoCard[][] board = i_ReceivedBoard.getBoard();
        int rows = i_ReceivedBoard.getNumOfRows();

        Data.setActiveGame(true);
        updateBuildingData(i_ReceivedBoard);
        Data.setFirstLines(createLines(board, rows, true, false));
    }

    private List<String> createLines(DtoCard[][] i_Board, int i_NumOfRows,
                                     boolean i_First, boolean i_Visible){
        List<String> lines = new ArrayList<>();

        for(int i = 0; i < i_NumOfRows; i++){
            StringBuilder line = new StringBuilder();
            for(DtoCard card : i_Board[i]){
                if(card.getGroup() == null){
                    line.append("|").append(alignString("", Data.getCardLineSize()));
                }
                else{
                    if (i_First) {
                        line.append("|").append(firstCardLine(card));
                    } else {
                        line.append("|").append(secondCardLine(card, i_Visible));
                    }
                }
            }

            line.append("|");
            lines.add(line.toString());
        }

        return lines;
    }

    private static String alignString(String i_String, int i_LineSize){
        int neededSpaces = i_LineSize - i_String.length();
        StringBuilder returnString = new StringBuilder();


        for(int i = 0; i < neededSpaces; i++){
            if(i == neededSpaces/2)
                returnString.append(i_String);
            returnString.append(" ");
        }

        return returnString.toString();
    }

    private String firstCardLine(DtoCard i_Card){
        return alignString(i_Card.getText(), Data.getCardLineSize());
    }

    private String secondCardLine(DtoCard i_Card, boolean i_Visible){
        String endText;
        String text;
        String groupName;

        DtoGroupCard group = i_Card.getGroup();

        if(group instanceof DtoGroupTeam){
            groupName = "(" + ((DtoGroupTeam)group).getName() + ")";
        }
        else if(((DtoGroupNeutral)group).isBlack()){
            groupName = "(" + BLACK + ")";
        }
        else{
            groupName = "";
        }

        endText = i_Visible?
                " " + (i_Card.isFlipped()? FLIPPED : NOT_FLIPPED) + " " + groupName :
                i_Card.isFlipped()? " " + FLIPPED + " " + groupName : "";

        text = "[" + i_Card.getID() + "]" + endText;

        return alignString(text, Data.getCardLineSize());
    }

    private void updateBuildingData(DtoBoard i_ReceivedBoard) {
        DtoCard[][] cardMatrix = i_ReceivedBoard.getBoard();
        int maxWordSize = getMaxWordLengthInCards(cardMatrix);
        int maxIdDigits = ((Integer)i_ReceivedBoard.getBoardSize()).toString().length();
        int maxTeamName = getMaxTeamName(i_ReceivedBoard.getCardGroups());
        int maxLineIdentification;
        int maxLineLength;

        maxLineIdentification = Integer.max(maxTeamName, NEUTRAL_MAX_NAME_LENGTH)
                + IDENTIFICATION_LINE_ADDONS_SIZE + maxIdDigits;
        maxLineLength = Integer.max(maxLineIdentification, maxWordSize);

        Data.setCardLineSize(maxLineLength, i_ReceivedBoard.getNumOfColumns());
    }

    private int getMaxWordLengthInCards(DtoCard[][] i_CardMatrix) {
        int maxWordSize = 0;

        for(DtoCard[] cardsRow : i_CardMatrix){
            Optional<Integer> maxInRow = Arrays.stream(cardsRow)
                    .filter(Objects::nonNull)
                    .map(DtoCard::getText)
                    .map(String::length)
                    .max(Integer::compareTo);

            if(maxInRow.isPresent() && maxInRow.get() > maxWordSize){
                maxWordSize = maxInRow.get();
            }
        }

        return maxWordSize;
    }

    private Integer getMaxTeamName(List<DtoGroupCard> i_CardGroup) {
        return i_CardGroup.stream()
                .filter(c -> c instanceof DtoGroupTeam)
                .map(DtoGroupTeam.class::cast)
                .map(DtoGroupTeam::getName)
                .map(String::length)
                .max(Integer::compareTo)
                .orElse(0);
    }
}
