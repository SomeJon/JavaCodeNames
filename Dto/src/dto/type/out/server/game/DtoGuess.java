package dto.type.out.server.game;

import dto.Dto;

public class DtoGuess implements Dto {
    public enum eDtoResult{
        HIT,
        MISS,
        BLACK_HIT
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
