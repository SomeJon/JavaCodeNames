package dto.type.out.server.game;

import dto.Dto;

public class DtoServerGuess implements Dto {
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
        },
        SKIP{
            @Override
            public String toString() {
                return "Skipped the turn!";
            }
        };
    }
    private final Integer guess;
    private final eDtoResult result;

    public DtoServerGuess(Integer guess, eDtoResult result) {
        this.guess = guess;
        this.result = result;
    }

    public Integer getGuess() {
        return guess;
    }

    public eDtoResult getResult() {
        return result;
    }
}
