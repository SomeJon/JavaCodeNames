package dto.type.out.server;

import engine.data.Team;

import java.util.ArrayList;
import java.util.List;

public class DtoServerInfo {
    private String ServerName;
    private String DictFileName;
    private int col;
    private int row;
    private final List<DtoServerTeam> ServerTeams;

    public DtoServerInfo() {
        ServerTeams = new ArrayList<DtoServerTeam>();
    }

    public void setServerName(String i_ServerName) {
        ServerName = i_ServerName;
    }

    public void setDictFileName(String i_DictFileName) {
        DictFileName = i_DictFileName;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int i_col) {
        col = i_col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int i_row) {
        row = i_row;
    }

    public String getServerName() {
        return ServerName;
    }

    public String getDictFileName() {
        return DictFileName;
    }

    public List<DtoServerTeam> getServerTeams() { return ServerTeams; }

    public void addServerTeam(Team i_Team, int i_AmountGuessers, int i_AmountDefiners) {
        DtoServerTeam team = new DtoServerTeam(i_Team, i_AmountGuessers, i_AmountDefiners);
        ServerTeams.add(team); //todo make sure error for not enough amount is returned
    }
}
