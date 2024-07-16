package jaxb.schema;

import engine.data.GameData;
import engine.data.GameStatus;
import engine.data.Team;
import jaxb.schema.ex01.generated.ECNGame;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class FileReader {
    private static final String JAXB_XML_GAME_PACKAGE_NAME = "jaxb.schema.ex01.generated";

    public static void ReadXml(File i_File, GameData i_DataHolder) throws JAXBException, IOException {

        InputStream inputStream = Files.newInputStream(i_File.toPath());
        ECNGame gameData = deserializeFrom(inputStream);

        String[] words;
        Set<String> normalWords;
        Set<String> blackWords;

        words = gameData.getECNWords().getECNGameWords().trim().split("[ \t\n]+");
        normalWords = new HashSet<String>(Arrays.asList(words));
        words = gameData.getECNWords().getECNGameWords().trim().split("[ \t\n]+");
        blackWords = new HashSet<String>(Arrays.asList(words));

        GameStatus status = loadStatus(gameData, normalWords.size(), blackWords.size());

        int columns = gameData.getECNBoard().getECNLayout().getColumns();
        int rows = gameData.getECNBoard().getECNLayout().getRows();
        i_DataHolder.loadData(status, columns, rows, normalWords, blackWords);

    }
    private static ECNGame deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (ECNGame) u.unmarshal(in);
    }

    private static GameStatus loadStatus(ECNGame gameData, int NormalWordsAmount, int BlackWordsAmount) {
        List<Team> Teams = new ArrayList<Team>();
        Team team = new Team(gameData.getECNTeam1().getName(), gameData.getECNTeam1().getCardsCount());
        Teams.add(team);
        team = new Team(gameData.getECNTeam2().getName(), gameData.getECNTeam2().getCardsCount());
        Teams.add(team);

        return new GameStatus(Teams, NormalWordsAmount, BlackWordsAmount,
                gameData.getECNBoard().getCardsCount(), gameData.getECNBoard().getBlackCardsCount());
    }

}
