package org.scoreborad.service;

import org.scoreborad.exception.GameNotFoundException;
import org.scoreborad.model.Game;
import org.scoreborad.model.ScoreBoard;
import org.scoreborad.utils.Utils;

import java.util.*;
import java.util.stream.Collectors;

public class ScoreBoardService {

    private final ScoreBoard scoreBoard;

    public ScoreBoardService(ScoreBoard scoreBoard) {
        this.scoreBoard = scoreBoard;
    }

    public ScoreBoard getScoreBoard() {
        return scoreBoard;
    }

    public void displayScoreBoard() {
        System.out.println(scoreBoard.getOngoingGames());
    }

    public void startGame(String homeTeam, String awayTeam) {
        scoreBoard.getOngoingGames().add(new Game(homeTeam, awayTeam, 0, 0));
    }

    public void finishGame(String teamName) {
        Game finishedGame = findGame(teamName);
        countTotalScore(finishedGame);
        scoreBoard.getPastGames().add(finishedGame);
        scoreBoard.getOngoingGames().remove(finishedGame);
    }

    /**
     * I've extended params (due to instructions it receives the pair of score only, if I understood correctly) because I
     * need team name to find relevant game since more than one game can be ongoing at the same time
     */
    public void updateScore(int homeTeamScore, int awayTeamScore, String teamName) {
        Game gameToUpdateScore = findGame(teamName);

        gameToUpdateScore.setHomeTeamScore(homeTeamScore);
        gameToUpdateScore.setAwayTeamScore(awayTeamScore);
    }

    public void displaySummaryByTotalScore() {
        System.out.println(getSummaryByTotalScore());
    }

    public List<Game> getSummaryByTotalScore() {
        return scoreBoard.getPastGames().stream()
                .sorted(Comparator.comparing(Game::getTotalScore)
                        .thenComparing(scoreBoard.getPastGames()::indexOf)
                        .reversed())
                .collect(Collectors.toList());
    }

    public Game findGame(String teamName) throws GameNotFoundException {
        return scoreBoard.getOngoingGames().stream()
                .filter(ongoingMatch ->
                        ongoingMatch.getHomeTeam().equals(teamName) ||
                                ongoingMatch.getAwayTeam().equals(teamName))
                .findFirst()
                .orElseThrow(() -> new GameNotFoundException(Utils.GAME_NOT_FOUND));
    }

    public void countTotalScore(Game game) {
        game.setTotalScore(game.getHomeTeamScore() + game.getAwayTeamScore());
    }
}
