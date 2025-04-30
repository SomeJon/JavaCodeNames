package data.server.data.game;

import dto.type.out.data.DtoGuessResult;
import dto.type.out.server.game.DtoServerGuess;

public class Guess {
    public enum eResult{
        HIT{
            @Override
            public DtoServerGuess.eDtoResult getDto() {
                return DtoServerGuess.eDtoResult.HIT;
            }
        },
        MISS{
            @Override
            public DtoServerGuess.eDtoResult getDto() {
                return DtoServerGuess.eDtoResult.MISS;
            }
        },
        BLACK_HIT{
            @Override
            public DtoServerGuess.eDtoResult getDto() {
                return DtoServerGuess.eDtoResult.BLACK_HIT;
            }
        },
        SKIP{
            @Override
            public DtoServerGuess.eDtoResult getDto() {
                return DtoServerGuess.eDtoResult.SKIP;
            }
        };

        public abstract DtoServerGuess.eDtoResult getDto();
    }
    private final Integer guess;
    private final eResult result;

    public Guess(Integer guess, eResult result) {
        this.guess = guess;
        this.result = result;
    }

    public Guess(Integer i_Guess, DtoGuessResult i_Result) {
        this.guess = i_Guess;

        switch (i_Result) {
            case NEUTRAL_HIT:
            case ENEMY_TEAM_HIT:
                result = eResult.MISS;
                break;
            case BLACK_HIT:
                result = eResult.BLACK_HIT;
                break;
            case SUCCESSFUL_GUESS:
                result = eResult.HIT;
                break;
            default:
                result = null;
                break;
        }
    }

    public Integer getGuess() {
        return guess;
    }

    public eResult getResult() {
        return result;
    }

    public DtoServerGuess getDto(){
        return new DtoServerGuess(guess, result.getDto());
    }
}
