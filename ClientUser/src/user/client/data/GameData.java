package user.client.data;

public class GameData {
    public enum roleChoice{
        IDENTIFIER{
            @Override
            public String toString() {
                return "Identifier";
            }

            @Override
            public int GetChoice() {
                return 0;
            }
        },
        GUESSER{
            @Override
            public String toString() {
                return "Guesser";
            }

            @Override
            public int GetChoice() {
                return 1;
            }
        };

        public abstract String toString();
        public abstract int GetChoice();
    }

    private String GameName = null;
    private Integer GameId = null;
    private String TeamName = null;
    private Integer TeamId = null;
    private roleChoice Role = null;

    public String getGameName() {
        return GameName;
    }

    public void setGameName(String i_GameName) {
        GameName = i_GameName;
    }

    public Integer getGameId() {
        return GameId;
    }

    public void setGameId(Integer i_GameId) {
        GameId = i_GameId;
    }

    public String getTeamName() {
        return TeamName;
    }

    public void setTeamName(String i_TeamName) {
        TeamName = i_TeamName;
    }

    public Integer getTeamId() {
        return TeamId;
    }

    public void setTeamId(Integer i_TeamId) {
        TeamId = i_TeamId;
    }

    public roleChoice getRole() {
        return Role;
    }

    public void setRole(roleChoice i_Role) {
        Role = i_Role;
    }

    public void clear(){
        GameId = null;
        TeamId = null;
        TeamName = null;
        Role = null;
        GameName = null;
    }
}
