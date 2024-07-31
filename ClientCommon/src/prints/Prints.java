package prints;

import dto.type.out.server.Choice.DtoServerGameChoice;
import dto.type.out.server.Choice.DtoServerTeamChoice;
import dto.type.out.server.Choice.DtoSubServerChoice;
import dto.type.out.server.DtoServerStatus;
import dto.type.out.server.DtoServerTeam;
import dto.type.out.server.DtoSubServerStatus;

public class Prints {
    public static String parseGamesStatus(DtoServerStatus i_Status, boolean i_PrintCurrentPlayers){
        StringBuilder ret = new StringBuilder();

        for(DtoSubServerStatus status : i_Status.getSubServerStatus()) {
            String state;
            String tab = "    ";
            if (status.isActive())
                state = "Active";
            else
                state = "Pending";
            ret.append("------------------------\n").append("Game Name: ").append(status.getServerName())
                    .append("\nStatus: ").append(state)
                    .append("\nBoard size (row X column): (").append(status.getRows()).append(" X ")
                    .append(status.getCols()).append(")\nName of dictionary file: ")
                    .append(status.getDictFileName()).append("\nNumber of participating cards: (Normal Cards - ")
                    .append(status.getNumOfCards()).append(") (Black Cards - ")
                    .append(status.getNumOfBlackCards()).append(") (Chosen from - ").append(status.getNumOfWords())
                    .append(" Words)\nTeams:\n");
            for (DtoServerTeam team : status.getServerTeams()) {
                ret.append("+++++++++++++++\n").append(tab)
                        .append("Team Name: ").append(team.getTeam().getName())
                        .append("\n").append(tab)
                        .append("Needed points - ").append(team.getTeam().getPointGoal())
                        .append("\n").append(tab);
                if (!i_PrintCurrentPlayers) {
                    ret.append("Definers - (")
                            .append(team.getNumOfDefiners())
                            .append(") Guessers - (")
                            .append(team.getNumOfGuessers())
                            .append(")\n");
                } else {
                    ret.append("(Connected/Needed)")
                            .append("\n").append(tab)
                            .append("Definers - (")
                            .append(team.getConnectedDefiners()).append("/")
                            .append(team.getNumOfDefiners()).append(")")
                            .append("\n").append(tab)
                            .append("Guessers - (")
                            .append(team.getConnectedGuessers()).append("/")
                            .append(team.getNumOfGuessers())
                            .append(")\n");
                }
            }
        }
        ret.append("------------------------\n");

        return ret.toString();
    }

    public static String parseGamesChoice(DtoServerGameChoice i_Choice){
        StringBuilder ret = new StringBuilder();

        for(DtoSubServerChoice choice : i_Choice.getSubServerChoices()){
            String tab = "    ";
            ret.append("------------------------\n")
                    .append("Game Id: ").append(choice.getId())
                    .append("\nGame Name: ").append(choice.getGameName())
                    .append("\nTeams:\n");
            for (DtoServerTeamChoice team : choice.getTeamChoices()) {
                ret.append("+++++++++++++++\n").append(tab)
                        .append("Team Id: ").append(team.getTeamId())
                        .append("\n").append(tab)
                        .append("Team Name: ").append(team.getTeamInfo().getTeam().getName())
                        .append("\n").append(tab)
                        .append("Needed points - ")
                        .append(team.getTeamInfo().getTeam().getPointGoal())
                        .append("\n").append(tab)
                        .append("(Connected/Needed)")
                        .append("\n").append(tab)
                        .append("Definers - (")
                        .append(team.getTeamInfo().getConnectedDefiners()).append("/")
                        .append(team.getTeamInfo().getNumOfDefiners()).append(")")
                        .append("\n").append(tab)
                        .append("Guessers - (")
                        .append(team.getTeamInfo().getConnectedGuessers()).append("/")
                        .append(team.getTeamInfo().getNumOfGuessers())
                        .append(")\n");
            }
        }
        ret.append("------------------------\n");

        return ret.toString();
    }

    public static String parseGamesChoiceAdmin(DtoServerGameChoice i_Choice){
        StringBuilder ret = new StringBuilder();
        int CounterActive = 0;
        int CounterAll = 0;

        for(DtoSubServerChoice choice : i_Choice.getSubServerChoices()){
            String tab = "    ";
            ret.append("------------------------\n")
                    .append("Game Id: ").append(choice.getId())
                    .append("\nGame Name: ").append(choice.getGameName())
                    .append("\nTeams(Active/All): ");
            for (DtoServerTeamChoice team : choice.getTeamChoices()) {
                CounterAll++;
                DtoServerTeam teamInfo = team.getTeamInfo();
                if(teamInfo.getConnectedDefiners() > 0
                        && teamInfo.getConnectedGuessers() > 0){
                    CounterActive++;
                }
            }
            ret.append("(")
                    .append(CounterActive)
                    .append("/")
                    .append(CounterAll)
                    .append(")\n");
        }
        ret.append("------------------------\n");

        return ret.toString();
    }
}
