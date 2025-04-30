package data.server.data.game;

import data.server.data.group.ServerTeam;
import dto.type.out.board.card.DtoGroupTeam;
import dto.type.out.server.game.DtoServerGuess;
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

    private final ServerTeam TurnTeam;
    private final int TeamId;
    private final int TurnNum;
    private DtoGroupTeam PlayingTeam;
    private DtoGroupTeam NextPlayingTeam;
    private eState State;
    private Identification TurnIdentification = new Identification();
    private final List<Guess> Guesses = new ArrayList<Guess>();
    private int GuessesLeft;


    public Turn(ServerTeam turnTeam, int teamId, int turnNum, DtoGroupTeam playingTeam, DtoGroupTeam nextPlayingTeam) {
        TurnTeam = turnTeam;
        TeamId = teamId;
        TurnNum = turnNum;
        PlayingTeam = playingTeam;
        State = eState.IDENTIFICATION;
        NextPlayingTeam = nextPlayingTeam;
    }

    public ServerTeam getTurnTeam() {
        return TurnTeam;
    }

    public void setPlayingTeam(DtoGroupTeam i_PlayingTeam) {
        PlayingTeam = i_PlayingTeam;
    }

    public void setNextPlayingTeam(DtoGroupTeam i_NextPlayingTeam) {
        NextPlayingTeam = i_NextPlayingTeam;
    }

    public int getTeamId() {
        return TeamId;
    }

    public int getTurnNum() {
        return TurnNum;
    }

    public DtoGroupTeam getPlayingTeam() {
        return PlayingTeam;
    }

    public DtoGroupTeam getNextPlayingTeam() {
        return NextPlayingTeam;
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
        List<DtoServerGuess> guesses = Guesses.stream()
                .map(Guess::getDto)
                .collect(Collectors.toList());

        return new DtoSingleTurnUpdate(State.getDto(), TeamId, TurnNum, PlayingTeam, NextPlayingTeam,
                TurnIdentification.getDto(), guesses, GuessesLeft);
    }
}
