package dto.type.out.server.game;

import dto.Dto;
import dto.type.out.board.DtoBoard;

public class DtoBoardUpdate implements Dto {
    private final DtoBoard Board;
    private final boolean GameEnd;

    public DtoBoardUpdate(DtoBoard board, boolean gameEnd) {
        Board = board;
        GameEnd = gameEnd;
    }

    public DtoBoard getBoard() {
        return Board;
    }

    public boolean isGameEnd() {
        return GameEnd;
    }
}
