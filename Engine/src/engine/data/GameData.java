package engine.data;

import engine.board.Board;
import exception.loadxml.OutOfBoundLoad;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

import static engine.board.Board.buildBoard;

public class GameData implements Serializable{
    private Set<String> Words = null;
    private Set<String> BlackWords = null;
    private int NumOfColumns;
    private int NumOfRows;
    private ActiveGame ActiveData = null;
    private GameStatus Status = null;


    public ActiveGame getActiveData() {
        return ActiveData;
    }

    public GameStatus getStatus() {
        return Status;
    }

    public void loadData
            (GameStatus i_Status, Integer i_NumOfColumns, Integer i_NumOfRows,
             Set<String> i_Words, Set<String> i_BlackWords) {
        if(i_NumOfColumns * i_NumOfRows < i_Status.getNumOfBlackCards() + i_Status.getNumOfCards()) {
            throw  new OutOfBoundLoad("Matrix Size",
                    i_Status.getNumOfBlackCards() + i_Status.getNumOfCards(),
                    i_NumOfColumns * i_NumOfRows, 0);
        }
        int a = i_NumOfColumns * i_NumOfRows;
        int b = i_Status.getNumOfBlackCards() + i_Status.getNumOfCards();
        int c = a + b;
        System.out.println(c);
        Status = i_Status;
        NumOfColumns = i_NumOfColumns;
        NumOfRows = i_NumOfRows;
        BlackWords = i_BlackWords;
        Words = i_Words;
    }

    public void startBoard() {
        Board newBoard = buildBoard(new ArrayList<>(Words), new ArrayList<>(BlackWords),
                Status, NumOfRows, NumOfColumns);
        ActiveData = new ActiveGame(newBoard, newBoard.getGroupTeams());
    }
}
