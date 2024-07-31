package dto.type.out.server.game;

import dto.Dto;

public class DtoIdentification implements Dto {
    private final String Identification;
    private final int RelatedWords;
    private final boolean Set;

    public DtoIdentification(String identification, int relatedWords, boolean set) {
        Identification = identification;
        RelatedWords = relatedWords;
        Set = set;
    }

    public int getRelatedWords() {
        return RelatedWords;
    }

    public String getIdentification() {
        return Identification;
    }

    public boolean isSet() {
        return Set;
    }
}
