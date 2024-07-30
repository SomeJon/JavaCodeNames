package dto.type.out.server.game;

import dto.Dto;

public class DtoGameUpdate implements Dto {
    private final DtoBoardUpdate BoardUpdate;
    private final DtoTurnsUpdate TurnsUpdate;

    public DtoGameUpdate(DtoBoardUpdate boardUpdate, DtoTurnsUpdate turnsUpdate){
        BoardUpdate = boardUpdate;
        TurnsUpdate = turnsUpdate;
    }

    public DtoBoardUpdate getBoardUpdate() {
        return BoardUpdate;
    }

    public DtoTurnsUpdate getTurnsUpdate() {
        return TurnsUpdate;
    }
}
