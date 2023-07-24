package org.scoreborad;

import org.scoreborad.model.ScoreBoard;
import org.scoreborad.service.ScoreBoardService;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ScoreBoard scoreBoard = new ScoreBoard(new ArrayList<>(), new ArrayList<>());
        ScoreBoardService service = new ScoreBoardService(scoreBoard);

        /** start six games and display them on the scoreboard */
        service.startGame("Mexico", "Canada");
        service.startGame("Moldova", "Poland");
        service.startGame("Spain", "Brazil");
        service.startGame("Germany", "France");
        service.startGame("Uruguay", "Italy");
        service.startGame("Argentina", "Australia");
        service.displayScoreBoard();

        /** finish two of those six games and see if they were removed from scoreboard */
        service.finishGame("Mexico");
        service.finishGame("Moldova");
        service.displayScoreBoard();

        /** update score on the rest of ongoing games and display scoreboard to see if it's been updated */
        service.updateScore(3, 5, "France");
        service.updateScore(9, 5, "Spain");
        service.updateScore(9, 5, "Uruguay");
        service.updateScore(1, 0, "Argentina");
        service.displayScoreBoard();

        /** finish all remaining games and display summary to see if games are properly ordered */
        service.finishGame("France");
        service.finishGame("Spain");
        service.finishGame("Uruguay");
        service.finishGame("Argentina");
        service.displaySummaryByTotalScore();
    }
}