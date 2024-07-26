package dto.type.out.server;

import dto.type.out.data.DtoTeam;
import engine.data.Team;

public class DtoServerTeam {
    private final DtoTeam Team;
    private final int NumOfGuessers;
    private final int NumOfDefiners;
    private final int ConnectedGuessers;
    private final int ConnectedDefiners;

    public DtoServerTeam(Team i_Team, int i_NumOfGuessers, int i_NumOfDefiners) {
        Team = new DtoTeam(i_Team);
        NumOfGuessers = i_NumOfGuessers;
        NumOfDefiners = i_NumOfDefiners;
        ConnectedGuessers = 0;
        ConnectedDefiners = 0;
    }

    public DtoServerTeam(Team team, int numOfGuessers, int numOfDefiners, int connectedGuessers, int connectedDefiners) {
        Team = new DtoTeam(team);
        NumOfGuessers = numOfGuessers;
        NumOfDefiners = numOfDefiners;
        ConnectedGuessers = connectedGuessers;
        ConnectedDefiners = connectedDefiners;
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

    public int getConnectedGuessers() {
        return ConnectedGuessers;
    }

    public int getConnectedDefiners() {
        return ConnectedDefiners;
    }
}
