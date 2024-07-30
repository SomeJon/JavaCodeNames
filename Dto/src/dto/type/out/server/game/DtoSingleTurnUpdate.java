package dto.type.out.server.game;

import dto.Dto;
import dto.type.out.board.card.DtoGroupTeam;

import java.util.List;

public class DtoSingleTurnUpdate implements Dto {
    public enum eDtoState {
        IDENTIFICATION,
        GUESSING
    }
    private final eDtoState TurnRole;
    private final int PlayingTeamId;
    private final DtoGroupTeam PlayingTeam;
    private final DtoIdentification TurnIdentification;
    private final List<DtoGuess> Guesses;
    private final int GuessesLeft;

    public DtoSingleTurnUpdate(eDtoState turnRole, int playingTeamId, DtoGroupTeam playingTeam,
                               DtoIdentification turnIdentification, List<DtoGuess> guesses, int guessesLeft) {
        TurnRole = turnRole;
        PlayingTeamId = playingTeamId;
        PlayingTeam = playingTeam;
        TurnIdentification = turnIdentification;
        Guesses = guesses;
        GuessesLeft = guessesLeft;
    }

    public eDtoState getTurnRole() {
        return TurnRole;
    }

    public int getPlayingTeamId() {
        return PlayingTeamId;
    }

    public DtoGroupTeam getPlayingTeam() {
        return PlayingTeam;
    }

    public DtoIdentification getTurnIdentification() {
        return TurnIdentification;
    }

    public List<DtoGuess> getGuesses() {
        return Guesses;
    }

    public int getGuessesLeft() {
        return GuessesLeft;
    }
}
