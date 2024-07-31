package dto.type.out.server.game;

import dto.Dto;

public class DtoGuess implements Dto {
    public enum eDtoResult{
        HIT{
            @Override
            public String toString() {
                return "Hit!";
            }
        },
        MISS{
            @Override
            public String toString() {
                return "Miss!";
            }
        },
        BLACK_HIT{
            @Override
            public String toString() {
                return "Hit a black card!";
            }
        };
    }
    private final String guess;
    private final eDtoResult result;

    public DtoGuess(String guess, eDtoResult result) {
        this.guess = guess;
        this.result = result;
    }

    public String getGuess() {
        return guess;
    }

    public eDtoResult getResult() {
        return result;
    }
}
