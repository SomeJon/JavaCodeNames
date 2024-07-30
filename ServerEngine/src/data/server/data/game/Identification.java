package data.server.data.game;

import dto.type.out.server.game.DtoIdentification;

public class Identification {
    private final String Identification;
    private final int RelatedWords;

    public Identification(String identification, int relatedWords) {
        Identification = identification;
        RelatedWords = relatedWords;
    }

    public int getRelatedWords() {
        return RelatedWords;
    }

    public String getIdentification() {
        return Identification;
    }

    public DtoIdentification getDto(){
        return new DtoIdentification(Identification, RelatedWords);
    }
}
