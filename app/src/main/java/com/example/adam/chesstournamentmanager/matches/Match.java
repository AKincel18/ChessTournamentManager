package com.example.adam.chesstournamentmanager.matches;

import com.example.adam.chesstournamentmanager.model.TournamentPlayer;

import java.io.Serializable;

public class Match implements Serializable {

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


    public String writeMatch(int pos){
        return pos + ". " + player1.getName() + " vs " + player2.getName();
    }

    public String writeResult(){
        switch (matchResult){
            case WHITE_WON:
                return "1-0";
            case DRAW:
                return "1/2 - 1/2";
            case BLACK_WON:
                return "0-1";
        }
        return null;

    }

    @Override
    public String toString() {
        return "Match{" +
                "round=" + round +
                ", "+ player1.getName() +
                " vs " + player2.getName() +
                ", matchResult=" + matchResult +
                '}';
    }
}
