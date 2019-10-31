package com.example.adam.chesstournamentmanager;


import android.util.Log;

import com.example.adam.chesstournamentmanager.model.Colors;
import com.example.adam.chesstournamentmanager.model.Players;
import com.example.adam.chesstournamentmanager.model.TournamentPlayer;
import com.example.adam.chesstournamentmanager.staticdata.Constans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

public class SwissAlgorithm {

    private int roundsNumber;

    private int currentRound;

    private int playersNumber;

    private List<TournamentPlayer> tournamentPlayers;

    private List<Boolean> isRoundFinish;

    private List<Match> matches = new ArrayList<>();

    private String order;

    private TournamentPlayer bye = new TournamentPlayer();

    private boolean even;

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
        even = playersNumber % 2 == 0;


    }

    private void drawNextRound(){

        TournamentPlayer byePlayer;
        if (!even) //not even, last player vs bye
            byePlayer= findByePlayer();
        else
            byePlayer = null;

        Iterator<List<TournamentPlayer>> groupIterator = groupsPlayers.listIterator();
        int listPos = 0;
        int playerPos = 0;
        while(listPos < groupsPlayers.size()){
            Iterator<TournamentPlayer> playerIterator = groupsPlayers.get(listPos).listIterator();
            while (playerIterator.hasNext()){
                TournamentPlayer player = playerIterator.next();
                if (!player.hasOpponent(currentRound)) {
                    if (!findOpponent(player, groupsPlayers.get(listPos).size(), listPos, playerPos)) { //not found opponent in his points group
                        //go into a lower group
                        if (listPos == groupsPlayers.size() - 1) { //huge problem :(((( -> in the last group, player hasn't opponent
                            Log.i("", "huge problem :(((( -> in the last group, player hasn't opponent");
                            break; //todo
                        }
                        else {

                            groupIterator.next();
                            ((ListIterator<List<TournamentPlayer>>) groupIterator).set(addingPlayerToGroup(groupsPlayers.get(listPos + 1), player));
                            ((ListIterator<List<TournamentPlayer>>) groupIterator).previous();
                            playerIterator.remove();
                        }
                    }
                }
                playerPos++;
            }
            listPos++;
            groupIterator.next();
        }

        if (!even)
            addMatchVersusBye(byePlayer);


    }

    private List<TournamentPlayer> addingPlayerToGroup(List<TournamentPlayer> list, TournamentPlayer player){
            list.add(0, player);
            return list;
    }


    private boolean findOpponent(TournamentPlayer player1, int playersInGroup, int group, int playersPos) {

        //find opponent in the second subgroup
        for (int i = playersInGroup / 2; i < playersInGroup; i++) {
            TournamentPlayer player2 = groupsPlayers.get(group).get(i);

            if (!player2.hasOpponent(currentRound) && !player1.isPlayedTogether(player2) && (player1!= player2) ) {
                addMatch(player1, player2);
                return true;
            }
        }

        //find opponent in the first subgroup (only for players from first subgroup)
        if (playersPos < playersInGroup / 2) {
            for (int i = 0; i < playersInGroup / 2; i++) {
                TournamentPlayer player2 = groupsPlayers.get(group).get(i);

                if (!player2.hasOpponent(currentRound) && !player1.isPlayedTogether(player2) && (player1!= player2) ) {
                    addMatch(player1, player2);
                    return true;
                }
            }
        }
        return false;

    }


    private void addMatchVersusBye(TournamentPlayer player){
        matches.add(new Match(currentRound, player, bye));
        player.setBye(true);
        player.setPrevOponents(bye);
        player.setPrevColors(Colors.NO_COLOR);
    }

    private void addMatch(TournamentPlayer player1, TournamentPlayer player2){
        if (drawColor()) { //TODO make intelligent color allocation
            matches.add(new Match(currentRound, player1, player2));
            player1.setPrevColors(Colors.WHITE);
            player2.setPrevColors(Colors.BLACK);
        }
        else{
            matches.add(new Match(currentRound, player2, player1));
            player1.setPrevColors(Colors.BLACK);
            player2.setPrevColors(Colors.WHITE);
        }

        player1.setPrevOponents(player2);
        player2.setPrevOponents(player1);
    }

    private TournamentPlayer findByePlayer(){

        int i = playersNumber - 1;
        while (i >= 0){
            TournamentPlayer player = tournamentPlayers.get(i);
            if (!player.hadByeBefore()){ //no bye before
/*                player.setBye(true);
                player.setPrevOponents(bye);*/
                removePlayerFromDrawing(player);
                Log.i("", "\t\t\t\t" + "BYE = " + player.toString());
                return player;
            }
            i--;
        }
        return null;
    }

    private void removePlayerFromDrawing(TournamentPlayer playerToRemove){
        for (List<TournamentPlayer> list : groupsPlayers){
            for (TournamentPlayer player : list){
                if (player == playerToRemove) {
                    list.remove(player);
                    break;
                }
            }
        }
    }

    private void drawRound(){
        int groupNumber = 0;
        for (int i = 0; i < groupsPlayers.size(); i++){
            //int x = xCoefficient(groupNumber);
            int groupSize = groupsPlayers.get(i).size();

            for (int j = 0; j < groupSize; j++){
                TournamentPlayer player = groupsPlayers.get(i).get(j);
                TournamentPlayer player2 = groupsPlayers.get(i).get(groupSize / 2 + j);

                if (!player.isPlayedTogether(player2)) { //is not played together
                    if (player.expectedColor() != player2.expectedColor()){
                        //losu losu
                        if (player.expectedColor() == Colors.WHITE){
                            matches.add(new Match(currentRound, player, player2)); //set color to player
                            player.setPrevColors(Colors.WHITE);
                            player2.setPrevColors(Colors.BLACK);
                        }
                        else {
                            matches.add(new Match(currentRound, player2, player));
                            player.setPrevColors(Colors.BLACK);
                            player2.setPrevColors(Colors.WHITE);
                        }
                        player.setPrevOponents(player2);
                        player2.setPrevOponents(player);

                    }
                    else {
                        //if (player.isColorRepatedTwoLastTimes())
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
            addMatch(player1, player2);
        }

        if (playersNumber % 2 != 0){
            TournamentPlayer player1 = tournamentPlayers.get(playersNumber - 1);
            matches.add(new Match(currentRound, player1, bye)); //last player vs bye
            player1.setPrevOponents(bye);
            player1.setPrevColors(Colors.NO_COLOR); //no color
            player1.setBye(true);
        }



        setResult(result());
        writeMatches();


        for (int i = 1; i< roundsNumber; i++){
            sortPlayerByPoints();
            groupsPlayers = prepareGroups();
            writeGroups();
            drawNextRound();
            setResult(result());
            writeMatches();
        }

        sortPlayerByPoints();
        writeEndResults();





    }

    private void writeEndResults(){
        for (TournamentPlayer player : tournamentPlayers){
            Log.i("", player.toString() + ", points = " + player.getPoints() + ", opponent = " + player.writeOpponent() +  " color = " + player.writeColors() );
        }
    }

    private void writeGroups(){
        for (List<TournamentPlayer> list : groupsPlayers){
            System.out.println(" ");
            System.out.println(" ");
            Log.i("", "\t\t\t\tGRUPA = " + groupsPlayers.indexOf(list));
            for (TournamentPlayer player : list){
                Log.i("","\t\t\t\tZAWODNIK = " + player.toString() + " punkty = " + player.getPoints());
            }
        }
    }
    private List<List<TournamentPlayer>> prepareGroups(){
        float pointsTemp = tournamentPlayers.get(0).getPoints(); //the best score
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

    private void writeAfterRound() {
        for (TournamentPlayer player : tournamentPlayers){
            Log.i("", "\t\t\t\t" + player.getSurname() + " " + player.getPoints());
        }
    }


    private MatchResult drawResult(){
        Random random = new Random();
        int i = random.nextInt(3);
        switch (i){
            case 0:
                return MatchResult.WHITE_WON;
            case 1:
                return MatchResult.BLACK_WON;
            case 2:
                return MatchResult.DRAW;
        }
        return MatchResult.WHITE_WON;
    }
    private List<MatchResult> result (){

        List<MatchResult> results = new ArrayList<>();

        int matchNumber;
        if (playersNumber % 2 == 0)
            matchNumber = playersNumber / 2;
        else
            matchNumber = playersNumber / 2 + 1;

        for (int i = 0; i < matchNumber; i++){
            if (i == matchNumber - 1 && (playersNumber / 2) % 2 == 1)
                results.add(MatchResult.WHITE_WON);
            else
                results.add(drawResult());
        }
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
        System.out.println(" ");
        System.out.println(" ");
        Log.i(" ", "\t\t\t\tRUNDA = " + (this.currentRound - 1));
        for (Match m : matches){
            if (m.getRound() == this.currentRound - 1)
                Log.i("","\t\t\t\t" + m.toString());
        }
    }
/*
    private void writeMatchesInCurrentRund(){

        Log.i("", "\t\t\t\tRUNDA= " + this.currentRound);
        for (Match match : matches){
            if (match.getRound() == this.currentRound){
                Log.i("","\t\t\t\t" + match.toString());
            }
        }
    }*/


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
