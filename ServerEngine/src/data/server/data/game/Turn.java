package data.server.data.game;

import dto.type.out.board.card.DtoGroupTeam;
import dto.type.out.server.game.DtoGuess;
import dto.type.out.server.game.DtoSingleTurnUpdate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Turn {
    public enum eState {
        IDENTIFICATION{
            @Override
            public DtoSingleTurnUpdate.eDtoState getDto() {
                return DtoSingleTurnUpdate.eDtoState.IDENTIFICATION;
            }
        },
        GUESSING{
            @Override
            public DtoSingleTurnUpdate.eDtoState getDto() {
                return DtoSingleTurnUpdate.eDtoState.GUESSING;
            }
        };

        public abstract DtoSingleTurnUpdate.eDtoState getDto();
    }

    private final int TeamId;
    private final DtoGroupTeam PlayingTeam;
    private eState State;
    private Identification TurnIdentification = null;
    private final List<Guess> Guesses = new ArrayList<Guess>();
    private int GuessesLeft;

    public Turn(int teamId, DtoGroupTeam playingTeam) {
        TeamId = teamId;
        PlayingTeam = playingTeam;
        State = eState.IDENTIFICATION;
    }

    public int getTeamId() {
        return TeamId;
    }

    public DtoGroupTeam getPlayingTeam() {
        return PlayingTeam;
    }

    public eState getState() {
        return State;
    }

    public void setState(eState i_State) {
        State = i_State;
    }

    public Identification getTurnIdentification() {
        return TurnIdentification;
    }

    public List<Guess> getGuesses() {
        return Guesses;
    }

    public void addGuess(Guess guess) {
        Guesses.add(guess);
    }

    public void setTurnIdentification(Identification turnIdentification) {
        TurnIdentification = turnIdentification;
        GuessesLeft = TurnIdentification.getRelatedWords();
    }

    public int getGuessesLeft() {
        return GuessesLeft;
    }

    public void guessDone(){
        GuessesLeft--;
    }

    public DtoSingleTurnUpdate getDto() {
        List<DtoGuess> guesses = Guesses.stream()
                .map(Guess::getDto)
                .collect(Collectors.toList());

        return new DtoSingleTurnUpdate(State.getDto(), TeamId, PlayingTeam,
                TurnIdentification.getDto(), guesses, GuessesLeft);
    }
}
