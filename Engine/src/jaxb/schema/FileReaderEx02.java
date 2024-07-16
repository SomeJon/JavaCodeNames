package jaxb.schema;

import dto.type.out.server.DtoServerInfo;
import engine.data.GameData;
import engine.data.GameStatus;
import engine.data.Team;
import jaxb.schema.ex02.generated.ECNGame;
import jaxb.schema.ex02.generated.ECNTeam;
import jaxb.schema.ex02.generated.ECNTeams;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class FileReaderEx02 {
    private static final String JAXB_XML_GAME_PACKAGE_NAME = "jaxb.schema.ex02.generated";

    public static void ReadFiles(InputStream i_XmlFile, InputStream i_WordDict,
                                        GameData i_DataHolder, DtoServerInfo i_DtoToFill) throws JAXBException, IOException {
        ECNGame gameData = deserializeFrom(i_XmlFile);
        String gameName = gameData.getName();
        String nameOfDict = gameData.getECNDictionaryFile(); //todo check this line
        i_DtoToFill.setServerName(gameName);
        i_DtoToFill.setDictFileName(nameOfDict);
        System.out.println(nameOfDict);

        String[] words;
        Set<String> wordsSet;
        //todo add check name of word dict
        words = readTxtStream(i_WordDict)
                .trim()
                .replaceAll("[!@#$%^&*)(-_,.?]","")
                .split("[ \t\n]+");
        wordsSet = new HashSet<String>(Arrays.asList(words));

        GameStatus status = loadStatus(gameData, wordsSet.size(), wordsSet.size(), i_DtoToFill);

        int columns = gameData.getECNBoard().getECNLayout().getColumns();
        int rows = gameData.getECNBoard().getECNLayout().getRows();
        i_DataHolder.loadData(status, columns, rows, wordsSet, wordsSet);

    }
    private static ECNGame deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (ECNGame) u.unmarshal(in);
    }

    private static String readTxtStream(InputStream i_TxtFile) throws IOException {
        Scanner scanner = new Scanner(i_TxtFile);
        StringBuilder text = new StringBuilder();
        while (scanner.hasNextLine()) {
            text.append(scanner.nextLine());
        }

        return text.toString();
    }

    private static GameStatus loadStatus(ECNGame gameData, int NormalWordsAmount,
                                         int BlackWordsAmount, DtoServerInfo i_ToFill) {
        List<Team> Teams = new ArrayList<Team>();
        ECNTeams ecnTeams = gameData.getECNTeams();

        for(ECNTeam ecnTeam : ecnTeams.getECNTeam()) {
            Team team = new Team(ecnTeam.getName(), ecnTeam.getCardsCount());
            Teams.add(team);
            i_ToFill.addServerTeam(team, ecnTeam.getGuessers(), ecnTeam.getDefiners());
        }


        return new GameStatus(Teams, NormalWordsAmount, BlackWordsAmount,
                gameData.getECNBoard().getCardsCount(), gameData.getECNBoard().getBlackCardsCount());
    }
}
