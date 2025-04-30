package dto.type.out.data;

import dto.Dto;
import dto.type.out.board.DtoBoard;
import dto.type.out.board.card.DtoGroupCard;
import dto.type.out.board.card.DtoGroupTeam;
import engine.board.Board;
import engine.board.card.GroupTeam;

public class DtoActiveGameStatus implements Dto {
    private final DtoBoard Board;
    private final DtoGroupTeam NextPlayingTeam;


    public DtoBoard getBoard() {
        return Board;
    }

    public DtoGroupTeam getNextPlayingTeam() {
        return NextPlayingTeam;
    }

    public DtoActiveGameStatus(Board i_Board, GroupTeam nextPlayingTeam) {
        Board = new DtoBoard(i_Board);
        NextPlayingTeam = (DtoGroupTeam) DtoGroupCard.getGroupCard(nextPlayingTeam.getCopy());
    }
}
