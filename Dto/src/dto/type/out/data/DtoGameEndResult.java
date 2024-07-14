package dto.type.out.data;

import dto.Dto;
import engine.board.card.GroupTeam;

public class DtoGameEndResult implements Dto {
    private final GroupTeam WinningTeam;
    private final DtoGuessResult GuessResult;

    public GroupTeam getWinningTeam() {
        return WinningTeam;
    }

    public DtoGuessResult getGuessResult() {
        return GuessResult;
    }

    public DtoGameEndResult(GroupTeam winningTeam, DtoGuessResult guessResult) {
        WinningTeam = (GroupTeam) winningTeam.getCopy();
        GuessResult = guessResult;
    }
}
