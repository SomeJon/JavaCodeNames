package dto.type.out.server;

import dto.type.out.data.DtoTeam;
import engine.data.Team;

public class DtoServerTeam {
    private final DtoTeam Team;
    private final int NumOfGuessers;
    private final int NumOfDefiners;

    public DtoServerTeam(Team i_Team, int i_NumOfGuessers, int i_NumOfDefiners) {
        Team = new DtoTeam(i_Team);
        NumOfGuessers = i_NumOfGuessers;
        NumOfDefiners = i_NumOfDefiners;
    }

    public DtoTeam getTeam() {
        return Team;
    }

    public int getNumOfGuessers() {
        return NumOfGuessers;
    }

    public int getNumOfDefiners() {
        return NumOfDefiners;
    }
}
