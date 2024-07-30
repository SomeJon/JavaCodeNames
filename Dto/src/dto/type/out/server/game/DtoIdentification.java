package dto.type.out.server.game;

import dto.Dto;

public class DtoIdentification implements Dto {
    private final String Identification;
    private final int RelatedWords;

    public DtoIdentification(String identification, int relatedWords) {
        Identification = identification;
        RelatedWords = relatedWords;
    }

    public int getRelatedWords() {
        return RelatedWords;
    }

    public String getIdentification() {
        return Identification;
    }
}
