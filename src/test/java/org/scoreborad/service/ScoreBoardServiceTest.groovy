package org.scoreborad.service

import org.scoreborad.exception.GameNotFoundException
import org.scoreborad.model.Game
import org.scoreborad.model.ScoreBoard
import org.scoreborad.service.ScoreBoardService
import spock.lang.Specification

class ScoreBoardServiceTest extends Specification {

    private ScoreBoardService service

    def setup() {
        ScoreBoard scoreBoard = new ScoreBoard(new ArrayList<Game>(), new ArrayList<Game>())
        service = new ScoreBoardService(scoreBoard)
    }

    def "DisplayScoreBoard- should return 2 ongoing games"() {
        given:
        service.startGame("Moldova", "Poland")
        service.startGame("Slovakia", "Czech Republic")

        when:
        service.displayScoreBoard()

        then:
        service.getScoreBoard().getOngoingGames().size() == 2
        noExceptionThrown()
    }

    def "StartGame- should start game between Moldova and Poland with initial score 0-0"() {
        given:
        String homeTeam = "Moldova"
        String awayTeam = "Poland"

        when:
        service.startGame(homeTeam, awayTeam)

        then:
        service.getScoreBoard().getOngoingGames().size() == 1
        service.getScoreBoard().getOngoingGames().get(0).getHomeTeam() == "Moldova"
        service.getScoreBoard().getOngoingGames().get(0).getAwayTeam() == "Poland"
        service.getScoreBoard().getOngoingGames().get(0).getHomeTeamScore() == 0
        service.getScoreBoard().getOngoingGames().get(0).getAwayTeamScore() == 0
    }

    def "FinishGame- should finish one game and move it to the past games /*lucky app, I still cannot move this game to the past :)*/"() {
        given:
        String teamName = "Moldova"
        service.startGame("Moldova", "Poland")

        when:
        service.finishGame(teamName)

        then:
        service.getScoreBoard().getOngoingGames().size() == 0
        service.getScoreBoard().getPastGames().size() == 1
    }

    def "FinishGame for invalid team- should throw GameNotFoundException"() {
        given:
        String teamName = "Sweden"
        service.startGame("Moldova", "Poland")

        when:
        service.finishGame(teamName)

        then:
        thrown(GameNotFoundException)
    }

    def "UpdateScore- should set score Spain 3:1 Brazil"() {
        given:
        service.startGame("Spain", "Brazil")
        String teamName = "Spain"

        when:
        service.updateScore(3, 1, teamName)

        then:
        service.findGame(teamName).getHomeTeamScore() == 3
        service.findGame(teamName).getAwayTeamScore() == 1
        noExceptionThrown()
    }

    def "UpdateScore for invalid team- should throw GameNotFoundException"() {
        given:
        service.startGame("England", "Sweden")
        String teamName = "Moldova"

        when:
        service.updateScore(3, 1, teamName)

        then:
        thrown(GameNotFoundException)
    }

    def "GetSummaryByTotalScore- should return Moldova-Poland first and then Uruguay-Italy as the newest of games with same amount of goals"() {
        given:
        service.startGame("Spain", "Brazil")
        service.startGame("Germany", "France")
        service.startGame("Uruguay", "Italy")
        service.startGame("Moldova", "Poland")

        service.updateScore(2, 1, "Spain")
        service.updateScore(2, 1, "France")
        service.updateScore(2, 1, "Italy")
        service.updateScore(3, 2, "Moldova")

        service.finishGame("Spain")
        service.finishGame("Germany")
        service.finishGame("Uruguay")
        service.finishGame("Moldova")

        when:
        service.getSummaryByTotalScore()

        then:
        service.getSummaryByTotalScore().get(0).getHomeTeam() == "Moldova"
        service.getSummaryByTotalScore().get(0).getAwayTeam() == "Poland"
        service.getSummaryByTotalScore().get(0).getTotalScore() == 5
        service.getSummaryByTotalScore().get(1).getHomeTeam() == "Uruguay"
        service.getSummaryByTotalScore().get(1).getAwayTeam() == "Italy"
        service.getSummaryByTotalScore().get(1).getTotalScore() == 3
        service.getSummaryByTotalScore().get(2).getTotalScore() == 3
    }

    def "FindGame- should return game between Moldova and Poland while searching by home team"() {
        given:
        service.startGame("Moldova", "Poland")

        when:
        def result = service.findGame("Moldova")

        then:
        result.getHomeTeam() == "Moldova"
        result.getAwayTeam() == "Poland"
        noExceptionThrown()
    }

    def "FindGame- should return game between Moldova and Poland while searching by away team"() {
        given:
        service.startGame("Moldova", "Poland")

        when:
        def result = service.findGame("Poland")

        then:
        result.getHomeTeam() == "Moldova"
        result.getAwayTeam() == "Poland"
        noExceptionThrown()
    }

    def "FindGame- should throw GameNotFondException"() {
        given:
        service.startGame("Moldova", "Poland")

        when:
        def result = service.findGame("Sweden")

        then:
        thrown(GameNotFoundException)
    }

    def "CountTotalScore- should return 5"() {
        given:
        service.startGame("Moldova", "Poland")
        service.updateScore(3, 2, "Moldova")
        def game = service.findGame("Moldova")

        when:
        service.countTotalScore(game)

        then:
        game.getTotalScore() == 5
    }
}
