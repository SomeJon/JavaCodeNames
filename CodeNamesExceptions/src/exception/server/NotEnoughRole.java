package exception.server;

import exception.CodeNameException;

import static exception.CodeNameException.ExceptionType.SERVER_GAME_LOGIC;

public class NotEnoughRole extends CodeNameException {
    private final String Role;
    private final String TeamName;

    public NotEnoughRole(String role, String teamName) {
        super.setType(SERVER_GAME_LOGIC);
        this.Role = role;
        this.TeamName = teamName;
    }

    public String getRole() {
        return Role;
    }

    public String getTeamName() {
        return TeamName;
    }
}
