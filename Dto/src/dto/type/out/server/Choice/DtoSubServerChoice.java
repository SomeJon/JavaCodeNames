package dto.type.out.server.Choice;

import dto.Dto;
import dto.type.out.server.HasActive;

import java.util.List;

public class DtoSubServerChoice implements HasActive, Dto {
    private final int Id;
    private final boolean Active;
    private final String GameName;
    private final List<DtoServerTeamChoice> TeamChoices;

    public DtoSubServerChoice(int id, boolean active, String gameName, List<DtoServerTeamChoice> teamChoices) {
        Id = id;
        GameName = gameName;
        TeamChoices = teamChoices;
        Active = active;
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

    public boolean isActive() {
        return Active;
    }
}
