package data.server.data.game;

import dto.type.out.server.game.DtoGuess;

public class Guess {
    public enum eResult{
        HIT{
            @Override
            public DtoGuess.eDtoResult getDto() {
                return DtoGuess.eDtoResult.HIT;
            }
        },
        MISS{
            @Override
            public DtoGuess.eDtoResult getDto() {
                return DtoGuess.eDtoResult.MISS;
            }
        },
        BLACK_HIT{
            @Override
            public DtoGuess.eDtoResult getDto() {
                return DtoGuess.eDtoResult.BLACK_HIT;
            }
        };

        public abstract DtoGuess.eDtoResult getDto();
    }
    private final String guess;
    private final eResult result;

    public Guess(String guess, eResult result) {
        this.guess = guess;
        this.result = result;
    }

    public String getGuess() {
        return guess;
    }

    public eResult getResult() {
        return result;
    }

    public DtoGuess getDto(){
        return new DtoGuess(guess, result.getDto());
    }
}
