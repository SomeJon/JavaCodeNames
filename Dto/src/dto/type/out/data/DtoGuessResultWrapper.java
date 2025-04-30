package dto.type.out.data;

import dto.type.out.board.card.DtoGroupTeam;

public class DtoGuessResultWrapper {
    private final DtoGuessResult GuessResult;
    private final DtoGroupTeam GroupTeam;

    public DtoGuessResultWrapper(DtoGuessResult guessResult) {
        GuessResult = guessResult;
        GroupTeam = GuessResult.getGroupTeam();
    }

    public DtoGuessResultWrapper(DtoGuessResult guessResult, DtoGroupTeam groupTeam) {
        GuessResult = guessResult;
        GroupTeam = groupTeam;
    }

    public DtoGuessResult getGuessResult() {
        return GuessResult;
    }

    public DtoGroupTeam getGroupTeam() {
        return GroupTeam;
    }
}
