package dto.type.out.server.game;

import dto.Dto;

public class DtoGameUpdate implements Dto {
    private final DtoBoardUpdate BoardUpdate;
    private final DtoSingleTurnUpdate TurnUpdate;

    public DtoGameUpdate(DtoBoardUpdate boardUpdate, DtoSingleTurnUpdate turnsUpdate){
        BoardUpdate = boardUpdate;
        TurnUpdate = turnsUpdate;
    }

    public DtoBoardUpdate getBoardUpdate() {
        return BoardUpdate;
    }

    public DtoSingleTurnUpdate getTurnUpdate() {
        return TurnUpdate;
    }
}
