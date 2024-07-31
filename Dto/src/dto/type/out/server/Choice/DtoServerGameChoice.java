package dto.type.out.server.Choice;

import dto.Dto;

import java.util.List;

public class DtoServerGameChoice implements Dto {
    private List<DtoSubServerChoice> SubServerChoices;

    public DtoServerGameChoice(List<DtoSubServerChoice> subServerChoices) {
        SubServerChoices = subServerChoices;
    }

    public DtoServerGameChoice() {
    }

    public List<DtoSubServerChoice> getSubServerChoices() {
        return SubServerChoices;
    }

    public void setSubServerChoices(List<DtoSubServerChoice> i_SubServerChoices) {
        SubServerChoices = i_SubServerChoices;
    }
}
