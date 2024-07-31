package data.server.data.game;

import dto.type.out.server.game.DtoIdentification;

public class Identification {
    private final String Identification;
    private final int RelatedWords;
    private final boolean Set;

    public Identification() {
        Identification = "";
        RelatedWords = 0;
        Set = false;
    }

    public Identification(String identification, int relatedWords) {
        Identification = identification;
        RelatedWords = relatedWords;
        Set = true;
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

    public DtoIdentification getDto(){
        return new DtoIdentification(Identification, RelatedWords, Set);
    }
}
