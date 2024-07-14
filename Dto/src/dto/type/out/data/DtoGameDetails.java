package dto.type.out.data;

import dto.Dto;
import engine.data.GameStatus;
import engine.data.Team;

import java.util.List;
import java.util.stream.Collectors;

public class DtoGameDetails implements Dto {
    private final List<Team> Teams;
    private final Integer NumOfWords;
    private final Integer NumOfBlackWords;
    private final Integer NumOfCards;
    private final Integer NumOfBlackCards;

    public DtoGameDetails(GameStatus i_Status) {
        Teams = i_Status.getTeams().stream()
                .map(Team::new)
                .collect(Collectors.toList());
        NumOfWords = i_Status.getNumOfWords();
        NumOfBlackWords = i_Status.getNumOfBlackWords();
        NumOfCards = i_Status.getNumOfCards();
        NumOfBlackCards = i_Status.getNumOfBlackCards();
    }

    public List<Team> getTeams() {
        return Teams;
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
}
