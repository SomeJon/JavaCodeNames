package engine.data;

import engine.board.Board;
import engine.board.card.GroupTeam;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ActiveGame implements Serializable {
    private final Board PlayingBoard;
    private GroupTeam PlayingTeamGroup;
    private final List<GroupTeam> PlayingTeams;

    public Board getPlayingBoard() {
        return PlayingBoard;
    }

    public GroupTeam getPlayingTeamGroup() {
        return PlayingTeamGroup;
    }

    public List<GroupTeam> getPlayingTeams() {
        return PlayingTeams;
    }

    public ActiveGame(engine.board.Board i_Board, List<GroupTeam> i_PlayingTeams) {
        PlayingBoard = i_Board;
        PlayingTeamGroup = i_PlayingTeams.get(0);
        PlayingTeams = new ArrayList<GroupTeam>(i_PlayingTeams);
    }

    public GroupTeam nextTeam(){
        PlayingTeamGroup = getNextTeam();

        return PlayingTeamGroup;
    }

    public void endCurrentTeam(){
        GroupTeam toRemove = PlayingTeamGroup;

        nextTeam();
        PlayingTeams.remove(toRemove);
    }

    public void endTeam(GroupTeam toRemove){
        PlayingTeams.remove(toRemove);
    }

    public GroupTeam getNextTeam(){
        int currentTeamIndex = PlayingTeams.indexOf(PlayingTeamGroup);
        int numberOfTeams = PlayingTeams.size();
        int nextTeamIndex = (currentTeamIndex + 1) % numberOfTeams;

        return PlayingTeams.get(nextTeamIndex);
    }
}
