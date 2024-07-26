package prints;

import dto.type.out.server.DtoServerStatus;
import dto.type.out.server.DtoServerTeam;
import dto.type.out.server.DtoSubServerStatus;

public class Prints {
    public static String parseGamesStatus(DtoServerStatus i_Status, boolean printCurrentPlayers){
        StringBuilder ret = new StringBuilder();

        for(DtoSubServerStatus status : i_Status.getSubServerStatus()){
            String state;
            String tab = "    ";
            if(status.isActive())
                state = "Active";
            else
                state = "Pending";
            ret.append("------------------------\n").append("Game Name: ").append(status.getServerName())
                    .append("\nStatus: ").append(state)
                    .append("\nBoard size (row X column): (").append(status.getRows()).append(" X ")
                    .append(status.getCols()).append(")\nName of dictionary file: ")
                    .append(status.getDictFileName()).append("\nNumber of participating cards: (Normal Cards - ")
                    .append(status.getNumOfCards()).append(") (Black Cards - ")
                    .append(status.getNumOfBlackCards()).append(") (Chosen from - " ).append(status.getNumOfWords())
                    .append(" Words)\nTeams:\n");
            for (DtoServerTeam team : status.getServerTeams()) {
                ret.append("+++++++++++++++\n").append(tab)
                            .append(team.getTeam().getName())
                            .append("\n").append(tab)
                            .append("Needed points - ")
                            .append(team.getTeam().getPointGoal())
                            .append("\n").append(tab);
                if (!printCurrentPlayers) {
                    ret.append("Definers - (")
                            .append(team.getNumOfDefiners())
                            .append(")\nGuessers - (")
                            .append(team.getNumOfGuessers())
                            .append(")\n");
                }
                else{
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
}
