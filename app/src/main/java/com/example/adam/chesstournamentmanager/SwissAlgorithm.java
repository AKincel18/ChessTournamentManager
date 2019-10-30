package com.example.adam.chesstournamentmanager;


import android.util.Log;

import com.example.adam.chesstournamentmanager.model.Colors;
import com.example.adam.chesstournamentmanager.model.Players;
import com.example.adam.chesstournamentmanager.model.TournamentPlayer;
import com.example.adam.chesstournamentmanager.staticdata.Constans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class SwissAlgorithm {

    private int roundsNumber;

    private int currentRound;

    private int playersNumber;

    private List<TournamentPlayer> tournamentPlayers;

    private List<Boolean> isRoundFinish;

    private List<Match> matches = new ArrayList<>();

    private String order;

    private List<List<TournamentPlayer>> groupsPlayers = new ArrayList<>(); //rows -> number of group, columns -> position in a group

    public SwissAlgorithm(int roundsNumber, String order) {
        this.roundsNumber = roundsNumber;
        this.order = order;
    }

    public void initTournamentPlayers(List<Players> players){

        tournamentPlayers = new ArrayList<>();

        for (Players p : players){
            tournamentPlayers.add(new TournamentPlayer(p));
        }
        playersNumber = tournamentPlayers.size();
        isRoundFinish = new ArrayList<>(roundsNumber);


    }

    private void drawRound(){
        int groupNumber = 0;
        for (int i = 0; i < groupsPlayers.size(); i++){
            int x = xCoefficient(groupNumber);
            int groupSize = groupsPlayers.get(i).size();

            for (int j = 0; j < groupSize; j++){
                TournamentPlayer player = groupsPlayers.get(i).get(j);
                TournamentPlayer player2 = groupsPlayers.get(i).get(groupSize / 2 + j);

                if (player.isGoodOponent(player2)) {
                    if (player.expectedColor() != player2.expectedColor()){
                        //losu losu
                        if (player.expectedColor() == Colors.WHITE){
                            matches.add(new Match(currentRound, player, player2)); //set color to player
                        }
                        else {
                            matches.add(new Match(currentRound, player2, player));//todo
                        }

                    }

                    //matches.add(new Match())
                }

/*
                if (player.getLastColor() != player2.getLastColor()){
                    matches.add(new Match(currentRound, player, player2));

                }*/

            }
            groupNumber++;
        }
    }

    private int xCoefficient(int groupNumber){
        return (groupsPlayers.get(groupNumber).size() / 2) - minColorExpected(groupNumber);
    }

    private int minColorExpected(int groupNumber){
        int whiteExpected = 0;
        int blackExpected = 0;

        for (TournamentPlayer player : groupsPlayers.get(groupNumber)){
            if (player.getLastColor() == Colors.WHITE)
                blackExpected++;
            else
                whiteExpected++;
        }
        return Math.min(whiteExpected, blackExpected);
    }


    public void drawFirstRound(){
        currentRound = 1;

        for (int i = 0; i < playersNumber / 2; i++){
            TournamentPlayer player1 = tournamentPlayers.get(i);
            TournamentPlayer player2 = tournamentPlayers.get(playersNumber / 2 + i);
            if (drawColor()){
                matches.add(new Match(currentRound,player1,player2));
                player1.setPrevColors(Colors.WHITE);
                player2.setPrevColors(Colors.BLACK);
            }
            else {
                matches.add(new Match(currentRound, player2, player1));
                player1.setPrevColors(Colors.BLACK);
                player2.setPrevColors(Colors.WHITE);
            }

            player1.setPrevOponents(player2);
            player2.setPrevOponents(player1);
        }
        if (playersNumber % 2 != 0){
            TournamentPlayer player1 = tournamentPlayers.get(playersNumber - 1);
            matches.add(new Match(currentRound, player1, new TournamentPlayer())); //last player vs bye
            player1.setPrevOponents(new TournamentPlayer());
            player1.setPrevColors(null); //no color
            player1.setBye(true);
        }

        writeMatches();
        setResult(result());

        writeAfterFirstRund();

        sortPlayerByPoints();

        groupsPlayers = prepareGroups();

        writeGroups();


    }

    private void writeGroups(){
        for (List<TournamentPlayer> list : groupsPlayers){
            Log.i("", "\t\t\t\tGRUPA = " + groupsPlayers.indexOf(list));
            for (TournamentPlayer player : list){
                Log.i("","\t\t\t\tZAWODNIK = " + player.toString() + " punkty = " + player.getPoints());
            }
        }
    }
    private List<List<TournamentPlayer>> prepareGroups(){
        float pointsTemp = (float) currentRound - 1; //max points scored
        groupsPlayers.clear(); //new drawing

        int currPos = 0;
        List<TournamentPlayer> list = new ArrayList<>();
        for (TournamentPlayer player : tournamentPlayers){

            boolean adding = true;
            while (player.getPoints() != pointsTemp){
                pointsTemp -= 0.5f;
                if (adding){
                    groupsPlayers.add(list);
                    list = new ArrayList<>();
                    adding = false;
                }
            }
            list.add(player);
            if (currPos == tournamentPlayers.size() - 1)
                groupsPlayers.add(list);

            currPos++;
        }

        return groupsPlayers;
    }
    private boolean drawColor(){
        Random randomGenerator = new Random();
        return randomGenerator.nextBoolean();
    }
    private void writeAfterFirstRund() {
        for (TournamentPlayer player : tournamentPlayers){
            Log.i("", "\t\t\t\t" + player.getSurname() + " " + player.getPoints() + " " +  player.getPrevOponents().get(0).getSurname());
        }
    }


    private List<MatchResult> result (){

        List<MatchResult> results = new ArrayList<>();
        results.add(MatchResult.BLACK_WON);
        results.add(MatchResult.WHITE_WON);
        results.add(MatchResult.DRAW);
        return results;

    }

    public void setResult(List<MatchResult> results){

        int pos = (currentRound - 1) * results.size();
        for (MatchResult matchResult : results){
            matches.get(pos).setMatchResult(matchResult);
            TournamentPlayer player1 = matches.get(pos).getPlayer1();
            TournamentPlayer player2 = matches.get(pos).getPlayer2();

            switch (matchResult){
                case WHITE_WON: //player1 won
                    player1.addPoints(1);
                    break;
                case BLACK_WON: //player2 won
                    player2.addPoints(1);
                    break;
                case DRAW:
                    player1.addPoints(0.5f);
                    player2.addPoints(0.5f);
                    break;
            }
            pos++;
        }

        currentRound++;


    }


    private void sortPlayerByPoints(){

        Collections.sort(tournamentPlayers, new Comparator<TournamentPlayer>() {
            @Override
            public int compare(TournamentPlayer o1, TournamentPlayer o2) {
                int c = Float.compare(o2.getPoints(), o1.getPoints()); //descending order

                if (c == 0){

                    switch (order){
                        case Constans.ALPHABETICAL_ORDER:

                            c = o1.getSurname().compareTo(o2.getSurname());
                            if (c == 0)
                                return o1.getName().compareTo(o2.getName());
                            return c;

                        case Constans.POLISH_RANKING_ORDER:

                            c = Integer.compare(o1.getPolishRanking(), o2.getPolishRanking());
                            if (c == 0)
                                c = o1.getSurname().compareTo(o2.getSurname());
                            if (c == 0)
                                return o1.getName().compareTo(o2.getName());
                            return c;

                        case Constans.INTERNATIONAL_RANKING_ORDER:

                            c = Integer.compare(o1.getInternationalRanking(), o2.getInternationalRanking());
                            if (c == 0)
                                c = o1.getSurname().compareTo(o2.getSurname());
                            if (c == 0)
                                return o1.getName().compareTo(o2.getName());
                            return c;

                        case Constans.RANDOM_ORDER:
                            Collections.shuffle(tournamentPlayers);
                            break;
                    }
                }
                return c;
            }
        });
    }



    private void writeMatches(){
        for (Match m : matches){
            Log.i("","\t\t\t\t" + m.toString());
        }
    }


    public int getRoundsNumber() {
        return roundsNumber;
    }

    public void setRoundsNumber(int roundsNumber) {
        this.roundsNumber = roundsNumber;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    public int getPlayersNumber() {
        return playersNumber;
    }

    public void setPlayersNumber(int playersNumber) {
        this.playersNumber = playersNumber;
    }

    public List<TournamentPlayer> getPlayers() {
        return tournamentPlayers;
    }

    public void setPlayers(List<TournamentPlayer> players) {
        this.tournamentPlayers = players;
    }

    public List<TournamentPlayer> getTournamentPlayers() {
        return tournamentPlayers;
    }

    public void setTournamentPlayers(List<TournamentPlayer> tournamentPlayers) {
        this.tournamentPlayers = tournamentPlayers;
    }

    public List<Boolean> getIsRoundFinish() {
        return isRoundFinish;
    }

    public void setIsRoundFinish(List<Boolean> isRoundFinish) {
        this.isRoundFinish = isRoundFinish;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }
}
