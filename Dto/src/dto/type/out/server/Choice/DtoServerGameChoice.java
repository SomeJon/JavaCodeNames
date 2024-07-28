package dto.type.out.server.Choice;

import java.util.List;

public class DtoServerGameChoice {
    private final List<DtoSubServerChoice> SubServerChoices;

    public DtoServerGameChoice(List<DtoSubServerChoice> subServerChoices) {
        SubServerChoices = subServerChoices;
    }

    public List<DtoSubServerChoice> getSubServerChoices() {
        return SubServerChoices;
    }
}
