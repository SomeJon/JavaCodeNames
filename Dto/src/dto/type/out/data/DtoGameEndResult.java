package dto.type.out.data;

import dto.Dto;
import dto.type.out.board.card.DtoGroupCard;
import dto.type.out.board.card.DtoGroupTeam;
import engine.board.card.GroupTeam;

public class DtoGameEndResult implements Dto {
    private final DtoGroupTeam WinningTeam;
    private final DtoGuessResult GuessResult;

    public DtoGroupTeam getWinningTeam() {
        return WinningTeam;
    }

    public DtoGuessResult getGuessResult() {
        return GuessResult;
    }

    public DtoGameEndResult(GroupTeam winningTeam, DtoGuessResult guessResult) {
        WinningTeam = (DtoGroupTeam) DtoGroupCard.getGroupCard(winningTeam.getCopy());
        GuessResult = guessResult;
    }
}
