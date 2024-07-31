package engine;

import dto.Dto;
import dto.type.out.board.card.DtoGroupTeam;
import dto.type.out.data.DtoActiveGameStatus;
import engine.board.card.Card;
import dto.type.out.data.DtoIdentification;
import dto.type.in.response.ingame.GuesserResponse;
import dto.type.in.response.ingame.IdentificationResponse;
import dto.type.in.response.Response;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public interface EngineInterface{
        public void loadFiles(Response i_LoadFiles) throws JAXBException, IOException;
        public Dto getStatus();
        public void startGame();
        public Dto getActiveBoard();
        public Dto getActiveTeam();
        public DtoIdentification playTurnIdentification(IdentificationResponse i_Response);
        public Dto playTurnGuessers(DtoIdentification i_CurrentIdentification, GuesserResponse i_Response);
        public Dto checkCardReturnContinue(Card i_GuessedCard);
        public DtoGroupTeam nextTeam();
        public Dto getNextTeam();
        public DtoActiveGameStatus getActiveGameStatus();
        public boolean didGameEng();
}
