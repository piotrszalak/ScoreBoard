package org.scoreborad.model;

import java.util.List;

public class ScoreBoard {

    private List<Game> ongoingGames;
    private List<Game> pastGames;

    public ScoreBoard(List<Game> ongoingGames, List<Game> pastGames) {
        this.ongoingGames = ongoingGames;
        this.pastGames = pastGames;
    }

    public List<Game> getOngoingGames() {
        return ongoingGames;
    }

    public void setOngoingGames(List<Game> ongoingGames) {
        this.ongoingGames = ongoingGames;
    }

    public List<Game> getPastGames() {
        return pastGames;
    }

    public void setPastGames(List<Game> pastGames) {
        this.pastGames = pastGames;
    }
}

