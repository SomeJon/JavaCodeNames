package dto.type.out.data;

import dto.Dto;

import java.io.Serializable;

public class DtoIdentification implements Serializable, Dto {
    private final String Identification;
    private final int Related;

    public String getIdentification() {
        return Identification;
    }

    public int getRelated() {
        return Related;
    }

    public DtoIdentification(String guess, int related) {
        Identification = guess;
        Related = related;
    }
}
