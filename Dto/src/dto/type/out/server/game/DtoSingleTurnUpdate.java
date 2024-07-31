package dto.type.out.server.game;

import dto.Dto;
import dto.type.out.board.card.DtoGroupTeam;

import java.util.List;

public class DtoSingleTurnUpdate implements Dto {
    public enum eDtoState {
        IDENTIFICATION{
            @Override
            public String toString() {
                return "Identification";
            }
        },
        GUESSING{
            @Override
            public String toString() {
                return "Guessing";
            }
        };

        public abstract String toString();
    }
    private final eDtoState TurnRole;
    private final int PlayingTeamId;
    private final int TurnNum;
    private final DtoGroupTeam PlayingTeam;
    private final DtoGroupTeam NextPlayingTeam;
    private final DtoIdentification TurnIdentification;
    private final List<DtoGuess> Guesses;
    private final int GuessesLeft;

    public DtoSingleTurnUpdate(eDtoState turnRole, int playingTeamId, int turnNum, DtoGroupTeam playingTeam,
                               DtoGroupTeam nextPlayingTeam, DtoIdentification turnIdentification,
                               List<DtoGuess> guesses, int guessesLeft) {
        TurnRole = turnRole;
        PlayingTeamId = playingTeamId;
        TurnNum = turnNum;
        PlayingTeam = playingTeam;
        NextPlayingTeam = nextPlayingTeam;
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

    public int getTurnNum() {
        return TurnNum;
    }

    public DtoGroupTeam getNextPlayingTeam() {
        return NextPlayingTeam;
    }
}
