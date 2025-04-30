package dto.type.out.server.game;

import dto.Dto;
import dto.type.out.board.card.DtoGroupTeam;
import dto.type.out.data.DtoGuessResult;

public class DtoEndResult implements Dto {
    private final DtoGuessResult Result;
    private final int Placement;
    private final boolean GameEnd;

    public DtoEndResult(){
        Result = DtoGuessResult.GAME_NOT_END;
        Placement = 0;
        GameEnd = false;
    }

    public DtoEndResult(DtoGuessResult result, int placement, boolean gameEnd) {
        Result = result;
        Placement = placement;
        GameEnd = gameEnd;
    }

    public DtoGuessResult getResult() {
        return Result;
    }

    public int getPlacement() {
        return Placement;
    }

    public boolean isEnd() {
        return Result != DtoGuessResult.GAME_NOT_END;
    }

    public boolean isGameEnd() {
        return GameEnd;
    }
}
