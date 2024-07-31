package data.server.data.game;

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
        };

        public abstract DtoServerGuess.eDtoResult getDto();
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

    public DtoServerGuess getDto(){
        return new DtoServerGuess(guess, result.getDto());
    }
}
