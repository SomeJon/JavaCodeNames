package engine.board.card;

import engine.data.Team;

public class GroupTeam extends CardGroup {
    private Team Team;

    public GroupTeam(Team i_Team, Integer i_NumOfCards) {
        CardsLeft = i_NumOfCards;
        Team = i_Team;
    }

    public String getName() {
        return Team.getName();
    }
}
