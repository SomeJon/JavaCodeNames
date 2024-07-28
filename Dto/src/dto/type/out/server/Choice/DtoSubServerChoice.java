package dto.type.out.server.Choice;

import java.util.List;

public class DtoSubServerChoice {
    private final int Id;
    private final String GameName;
    private final List<DtoServerTeamChoice> TeamChoices;

    public DtoSubServerChoice(int id, String gameName, List<DtoServerTeamChoice> teamChoices) {
        Id = id;
        GameName = gameName;
        TeamChoices = teamChoices;
    }

    public int getId() {
        return Id;
    }

    public String getGameName() {
        return GameName;
    }

    public List<DtoServerTeamChoice> getTeamChoices() {
        return TeamChoices;
    }
}
