package dto.type.out.server;

import dto.type.out.data.DtoGameDetails;

import java.util.List;

public class DtoSubServerStatus {
    private final String ServerName;
    private final String DictFileName;
    private final List<DtoServerTeam> ServerTeams;
    private final Integer NumOfWords;
    private final Integer NumOfBlackWords;
    private final Integer NumOfCards;
    private final Integer NumOfBlackCards;
    private final Integer Rows;
    private final Integer Cols;
    private boolean Active = false;

    public DtoSubServerStatus(DtoServerInfo i_Status, DtoGameDetails i_GameDetails) {
        ServerName = i_Status.getServerName();
        DictFileName = i_Status.getDictFileName();
        ServerTeams = i_Status.getServerTeams();
        NumOfWords = i_GameDetails.getNumOfWords();
        NumOfBlackWords = i_GameDetails.getNumOfBlackWords();
        NumOfCards = i_GameDetails.getNumOfCards();
        NumOfBlackCards = i_GameDetails.getNumOfBlackCards();
        Rows = i_Status.getRow();
        Cols = i_Status.getCol();
    }

    public String getServerName() {
        return ServerName;
    }

    public String getDictFileName() {
        return DictFileName;
    }

    public List<DtoServerTeam> getServerTeams() {
        return ServerTeams;
    }

    public Integer getNumOfWords() {
        return NumOfWords;
    }

    public Integer getNumOfBlackWords() {
        return NumOfBlackWords;
    }

    public Integer getNumOfCards() {
        return NumOfCards;
    }

    public Integer getNumOfBlackCards() {
        return NumOfBlackCards;
    }

    public Integer getRows() {
        return Rows;
    }

    public Integer getCols() {
        return Cols;
    }

    public boolean isActive() {
        return Active;
    }

    public void setActive(boolean i_Active) {
        Active = i_Active;
    }
}
