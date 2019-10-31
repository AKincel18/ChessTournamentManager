package com.example.adam.chesstournamentmanager;

import com.example.adam.chesstournamentmanager.model.TournamentPlayer;

public class Match {

    private int round;

    private TournamentPlayer player1;

    private TournamentPlayer player2;

    private MatchResult matchResult;

    public Match(int round, TournamentPlayer player1, TournamentPlayer player2) {
        this.round = round;
        this.player1 = player1;
        this.player2 = player2;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public TournamentPlayer getPlayer1() {
        return player1;
    }

    public void setPlayer1(TournamentPlayer player1) {
        this.player1 = player1;
    }

    public TournamentPlayer getPlayer2() {
        return player2;
    }

    public void setPlayer2(TournamentPlayer player2) {
        this.player2 = player2;
    }

    public MatchResult getMatchResult() {
        return matchResult;
    }

    public void setMatchResult(MatchResult matchResult) {
        this.matchResult = matchResult;
    }

/*    @Override
    public String toString() {
        return "Match{" +
                "round=" + round +
                ", player1=" + player1.getName() + " " + player1.getSurname() +
                ", player2=" + player2.getName() + " " + player2.getSurname() +
                ", matchResult=" + matchResult +
                '}';
    }*/

    @Override
    public String toString() {
        return "Match{" +
                "round=" + round +
                ","+ player1 +
                " vs" + player2 +
                ", matchResult=" + matchResult +
                '}';
    }
}
