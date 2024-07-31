package dto.type.out.board;

import dto.Dto;
import dto.type.out.board.card.DtoCard;
import dto.type.out.board.card.DtoGroupCard;
import dto.type.out.board.card.DtoGroupTeam;
import engine.board.Board;
import engine.board.Position;
import engine.board.card.Card;
import engine.board.card.GroupCard;
import engine.board.card.GroupTeam;


import java.util.List;
import java.util.stream.Collectors;

public class DtoBoard implements Dto {
    private final DtoCard[][] Board;
    private final List<DtoGroupCard> CardGroups;
    private final List<DtoGroupTeam> GroupTeams;
    private final int NumOfColumns;
    private final int NumOfRows;

    public DtoBoard(DtoCard[][] board, List<DtoGroupCard> cardGroups, List<DtoGroupTeam> groupTeams, int numOfColumns, int numOfRows) {
        Board = board;
        CardGroups = cardGroups;
        GroupTeams = groupTeams;
        NumOfColumns = numOfColumns;
        NumOfRows = numOfRows;
    }

    public int getBoardSize(){
        return NumOfRows * NumOfColumns;
    }

    public DtoCard[][] getBoard() {
        return Board;
    }

    public List<DtoGroupCard> getCardGroups() {
        return CardGroups;
    }

    public List<DtoGroupTeam> getGroupTeams() {
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
        Board = new DtoCard[NumOfRows][NumOfColumns];
        int index = 1;

        List<GroupCard> cardGroups = i_Board.getCardGroups().stream()
                .map(GroupCard::getCopy)
                .collect(Collectors.toList());

        CardGroups = cardGroups.stream()
                .map(DtoGroupCard::getGroupCard)
                .collect(Collectors.toList());

        GroupTeams = i_Board.getGroupTeams().stream()
                .map(GroupTeam::getCopy)
                .map(DtoGroupCard::getGroupCard)
                .map(DtoGroupTeam.class::cast)
                .collect(Collectors.toList());

        for(Card[] cardRow:i_Board.getBoard()){
            for(Card card:cardRow){
                Position pos = Position.getPostion(index, NumOfColumns);
                DtoCard cardToAdd;
                if(card.getGroup() == null) {
                    cardToAdd = new DtoCard(new Card("", null));
                }
                else {
                    cardToAdd = new DtoCard(new Card(card.getText(),
                            cardGroups.get(cardGroups.indexOf(card.getGroup())), card.getID(), card.isFlipped()));
                }
                Board[pos.getRow()][pos.getCol()] = cardToAdd;
                index++;
            }
        }
    }
}
