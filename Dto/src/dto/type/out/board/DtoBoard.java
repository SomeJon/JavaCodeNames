package dto.type.out.board;

import dto.Dto;
import engine.board.Board;
import engine.board.Position;
import engine.board.card.Card;
import engine.board.card.GroupCard;
import engine.board.card.GroupTeam;


import java.util.List;
import java.util.stream.Collectors;

public class DtoBoard implements Dto {
    private final Card[][] Board;
    private final List<GroupCard> CardGroups;
    private final List<GroupTeam> GroupTeams;
    private final int NumOfColumns;
    private final int NumOfRows;

    public int getBoardSize(){
        return NumOfRows * NumOfColumns;
    }

    public Card[][] getBoard() {
        return Board;
    }

    public List<GroupCard> getCardGroups() {
        return CardGroups;
    }

    public List<GroupTeam> getGroupTeams() {
        return GroupTeams;
    }

    public int getNumOfColumns() {
        return NumOfColumns;
    }

    public int getNumOfRows() {
        return NumOfRows;
    }

    public DtoBoard(Board i_Board) {
        NumOfColumns = i_Board.getNumOfColumns();
        NumOfRows = i_Board.getNumOfRows();
        Board = new Card[NumOfRows][NumOfColumns];
        int index = 1;
        CardGroups = i_Board.getCardGroups().stream()
                .map(GroupCard::getCopy)
                .collect(Collectors.toList());

        GroupTeams = i_Board.getGroupTeams().stream()
                .map(GroupTeam::getCopy)
                .map(GroupTeam.class::cast)
                .collect(Collectors.toList());

        for(Card[] cardRow:i_Board.getBoard()){
            for(Card card:cardRow){
                Position pos = Position.getPostion(index, NumOfColumns);
                Card cardToAdd;
                if(card.getGroup() == null) {
                    cardToAdd = new Card("", null);
                }
                else {
                    cardToAdd = new Card(card.getText(),
                            CardGroups.get(CardGroups.indexOf(card.getGroup())), card.getID(), card.isFlipped());
                }
                Board[pos.getRow()][pos.getCol()] = cardToAdd;
                index++;
            }
        }
    }
}
