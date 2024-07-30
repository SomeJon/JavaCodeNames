package dto.type.out.server.game;

import dto.Dto;

import java.util.List;

public class DtoTurnsUpdate implements Dto {
    private final List<DtoSingleTurnUpdate> Turns;
    private final boolean SemiUpdate;

    public DtoTurnsUpdate(List<DtoSingleTurnUpdate> turns, boolean semiUpdate) {
        Turns = turns;
        SemiUpdate = semiUpdate;
    }

    public List<DtoSingleTurnUpdate> getTurns() {
        return Turns;
    }

    public boolean isSemiUpdate() {
        return SemiUpdate;
    }
}
