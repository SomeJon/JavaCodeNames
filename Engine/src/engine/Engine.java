package engine;

import dto.Dto;
import dto.type.in.response.*;
import dto.type.in.response.ingame.GuesserResponse;
import dto.type.in.response.ingame.IdentificationResponse;
import dto.type.in.response.load.LoadInputStreamsResponse;
import dto.type.in.response.load.LoadXmlResponse;
import dto.type.out.board.DtoBoard;
import dto.type.out.board.card.DtoGroupTeam;
import dto.type.out.data.*;
import engine.board.Board;
import engine.board.card.Card;
import engine.board.card.GroupCard;
import engine.board.card.GroupNeutral;
import engine.board.card.GroupTeam;
import engine.data.GameData;
import engine.data.Identification;
import exception.turn.CardFlippedException;
import exception.turn.GuessOutOfRangeException;
import exception.turn.IdentificationException;
import jaxb.schema.FileReader;
import jaxb.schema.FileReaderEx02;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class Engine implements EngineInterface, Serializable {

    private final GameData Data;

    public Engine(GameData i_Data) {
        Data = i_Data;
    }

    @Override
    public void loadFiles(Response i_LoadFiles) throws JAXBException, IOException {
        if(i_LoadFiles instanceof LoadInputStreamsResponse) {
            LoadInputStreamsResponse response = (LoadInputStreamsResponse) i_LoadFiles;
            FileReaderEx02.ReadFiles(response.getXmlInputStream(), response.getTxtInputStream(),
                    response.getTxtFileName(), Data, response.getDtoToLoad());
        }
        else {
            LoadXmlResponse loadXml = (LoadXmlResponse) i_LoadFiles;
            File responseFile = loadXml.getXmlFile();
            FileReader.ReadXml(responseFile, Data);
        }
    }

    @Override
    public Dto getStatus(){
        return new DtoGameDetails(Data.getStatus());
    }

    @Override
    public void startGame() {
        Data.startBoard();
    }

    @Override
    public Dto getActiveBoard() {
        return new DtoBoard(Data.getActiveData().getPlayingBoard());
    }

    @Override
    public Dto getActiveTeam() {
        return new DtoGroupTeam(Data.getActiveData().getPlayingTeamGroup());
    }

    @Override
    public Identification playTurnIdentification(IdentificationResponse i_Response) {
        GroupTeam playingTeam = Data.getActiveData().getPlayingTeamGroup();

        if (i_Response.getRelated() >
                playingTeam.getCards() - playingTeam.getCardsFlipped() || i_Response.getRelated() < 1) {
            throw new IdentificationException("Related words", i_Response.getRelated(),
                    playingTeam.getCards() - playingTeam.getCardsFlipped(), 1);
        }

        return new Identification(i_Response.getIdentification(), i_Response.getRelated());
    }

    @Override
    public Dto playTurnGuessers(Identification i_CurrentIdentification, GuesserResponse i_Response){
        GroupTeam playingTeam = Data.getActiveData().getPlayingTeamGroup();
        Board playingBoard = Data.getActiveData().getPlayingBoard();
        int maxId = Data.getStatus().getNumOfCards() + Data.getStatus().getNumOfBlackCards();
        int cardId = i_Response.getCardId();
        Dto ret;

        if(cardId < 0 || cardId > maxId) {
                throw new GuessOutOfRangeException("Card Id", cardId, maxId, 1);
        } else {
            Card guessedCard = playingBoard.getCard(cardId);
            if (guessedCard.isFlipped()) {
                throw new CardFlippedException();
            } else {
                guessedCard.flip();
                ret = checkCardReturnContinue(guessedCard);
            }
        }

        return ret;
    }

    @Override
    public Dto checkCardReturnContinue(Card i_GuessedCard) {
        GroupTeam playingTeam = Data.getActiveData().getPlayingTeamGroup();
        GroupCard cardGroup = i_GuessedCard.getGroup();
        Dto returnedValue;

        if(cardGroup instanceof GroupNeutral) {
            GroupNeutral NeutralGroup = (GroupNeutral) cardGroup;
            if (NeutralGroup.isBlack()) {
                DtoGuessResult.BLACK_HIT.setGroupTeam(new DtoGroupTeam(playingTeam));
                Data.getActiveData().endCurrentTeam();
                if (Data.getActiveData().getPlayingTeams().size() == 1) {
                    returnedValue = new DtoGameEndResult(Data.getActiveData().getPlayingTeamGroup(), DtoGuessResult.BLACK_HIT);
                } else {
                    returnedValue = DtoGuessResult.BLACK_HIT;
                }
            } else {
                returnedValue = DtoGuessResult.NEUTRAL_HIT;
            }
        }
        else {
            GroupTeam groupTeam = (GroupTeam) cardGroup;
            if (groupTeam != playingTeam) {
                DtoGuessResult.ENEMY_TEAM_HIT.setGroupTeam(new DtoGroupTeam(groupTeam));
                if(groupTeam.getCardsFlipped() == groupTeam.getCards()) {
                    Data.getActiveData().endTeam(groupTeam);
                    returnedValue = new DtoGameEndResult(groupTeam, DtoGuessResult.ENEMY_TEAM_HIT);
                }
                else{
                    returnedValue = DtoGuessResult.ENEMY_TEAM_HIT;
                }
            } else {
                if (groupTeam.getCardsFlipped() == groupTeam.getCards()) {
                    Data.getActiveData().endCurrentTeam();
                    returnedValue = new DtoGameEndResult(groupTeam, DtoGuessResult.SUCCESSFUL_GUESS);
                } else {
                    returnedValue = DtoGuessResult.SUCCESSFUL_GUESS;
                }
            }
        }

        return returnedValue;
    }

    @Override
    public DtoGroupTeam nextTeam() {
        GroupTeam nextTeam = Data.getActiveData().nextTeam();
        return new DtoGroupTeam(nextTeam);
    }

    @Override
    public Dto getNextTeam() {
        GroupTeam nextTeam = Data.getActiveData().getNextTeam();
        return new DtoGroupTeam(nextTeam);
    }

    @Override
    public DtoActiveGameStatus getActiveGameStatus() {
        return new DtoActiveGameStatus(Data.getActiveData().getPlayingBoard(),
                Data.getActiveData().getPlayingTeamGroup());
    }

    @Override
    public boolean didGameEng() {
        return Data.getActiveData().getPlayingTeams().size() == 1;
    }


}
