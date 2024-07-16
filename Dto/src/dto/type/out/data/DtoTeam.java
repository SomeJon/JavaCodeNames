package dto.type.out.data;

import engine.data.Team;

public class DtoTeam {
    private final String Name;
    private final int PointGoal;

    public DtoTeam(Team team) {
        Name = team.getName();
        PointGoal = team.getPointGoal();
    }

    public String getName() {
        return Name;
    }

    public int getPointGoal() {
        return PointGoal;
    }
}
