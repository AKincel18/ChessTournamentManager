package com.example.adam.chesstournamentmanager.swissalgorithm;


import android.util.Log;

import com.example.adam.chesstournamentmanager.matches.Match;
import com.example.adam.chesstournamentmanager.matches.MatchResult;
import com.example.adam.chesstournamentmanager.model.Colors;
import com.example.adam.chesstournamentmanager.model.Player;
import com.example.adam.chesstournamentmanager.model.TournamentPlayer;
import com.example.adam.chesstournamentmanager.staticdata.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

public class SwissAlgorithm implements Serializable {

    private static SwissAlgorithm INSTANCE;

    private int roundsNumber;

    private int currentRound = 1;

    private int playersNumber;

    private List<TournamentPlayer> tournamentPlayers;

    private List<List<Match>> matches = new ArrayList<>(); //rows -> number of rounds, columns -> matches

    private List<Match> matchesTmp = new ArrayList<>();

    private boolean finishedTournament = false;

    private boolean even;

    private List<List<TournamentPlayer>> groupsPlayers = new ArrayList<>(); //rows -> number of group, columns -> position in a group

    private boolean placeOrder; // true - buchholz, false - median buchholz

    private SwissAlgorithm(int roundsNumber, boolean placeOrder) {
        this.roundsNumber = roundsNumber;
        this.placeOrder = placeOrder;
    }

    public static SwissAlgorithm initSwissAlgorithm(int roundsNumber, boolean placeOrder) {
        if (INSTANCE == null) {
            INSTANCE = new SwissAlgorithm(roundsNumber, placeOrder);
        }
        return INSTANCE;
    }

    public static SwissAlgorithm getINSTANCE() {
        return INSTANCE;
    }

    public static void resetTournament() {
        INSTANCE = null;
    }

    public void initTournamentPlayers(List<Player> players) {

        tournamentPlayers = new ArrayList<>();

        for (Player p : players) {
            tournamentPlayers.add(new TournamentPlayer(p));
        }
        playersNumber = tournamentPlayers.size();
        even = playersNumber % 2 == 0;

    }

    public void drawFirstRound() {

        for (int i = 0; i < playersNumber / 2; i++) {
            TournamentPlayer player1 = tournamentPlayers.get(i);
            TournamentPlayer player2 = tournamentPlayers.get(playersNumber / 2 + i);
            addFirstMatchRound(player1, player2);

        }

        if (playersNumber % 2 != 0) {
            TournamentPlayer player1 = tournamentPlayers.get(playersNumber - 1);
            addMatchVersusBye(player1);
        }

        matches.add(matchesTmp);
        matchesTmp = new ArrayList<>();
    }


    private void reset() {
        for (TournamentPlayer player : tournamentPlayers) {
            player.setUpper(false);
        }
    }

    public void drawNextRound() {
        sortPlayerDuringTournament();
        groupsPlayers = prepareGroups();

        reset();
        TournamentPlayer byePlayer = null;
        if (!even) //not even, last player vs bye
            byePlayer = findByePlayer();

        Iterator<List<TournamentPlayer>> groupIterator = groupsPlayers.listIterator();
        int groupNumber = 0;
        //int playerPos = 0;
        while (groupNumber < groupsPlayers.size()) {
            boolean removeEmptyGroup = false;
            boolean backToHigherGroup = false;
            Iterator<TournamentPlayer> playerIterator = groupsPlayers.get(groupNumber).listIterator();

            while (playerIterator.hasNext()) {
                TournamentPlayer player = playerIterator.next();
                if (!player.hasOpponent(currentRound)) {
                    if (!findOpponent(player, groupsPlayers.get(groupNumber).size(), groupNumber)) { //not found opponent in his points group

                        if (groupNumber == 0) {
                            player.setUpper(false);
                        }

                        //go to higher group
                        if (player.isUpper() || groupNumber == groupsPlayers.size() - 1) {

                            groupNumber--;
                            groupsPlayers.get(groupNumber).add(0, player); //add player to higher group, on the first position
                            playerIterator.remove(); //remove player from current group
                            removeMatchesInGroup(groupNumber); //remove all matches from higher group
                            backToHigherGroup = true; //set flag
                            break;

                        } else {

                            //go to lower group
                            groupsPlayers.get(groupNumber + 1).add(0, player);
                            if (groupNumber + 1 == groupsPlayers.size() - 1)
                                player.setUpper(true);

                            removeMatchesInGroup(groupNumber + 1);
                            playerIterator.remove();


                        }

                        //go into a higher group
                        //remove empty group
                        if (groupsPlayers.get(groupNumber).isEmpty()) {
                            List<TournamentPlayer> list = groupIterator.next();
                            groupIterator.remove();
                            removeEmptyGroup = true;
                        }
                    }
                }
            }

            if (removeEmptyGroup || backToHigherGroup) {
                groupIterator = groupsPlayers.listIterator(groupNumber);
            } else {
                groupNumber++;
                groupIterator.next();
            }

        }

        if (!even)
            addMatchVersusBye(byePlayer); //add the last one match


        matches.add(matchesTmp);
        matchesTmp = new ArrayList<>();
    }

    private void buchholzMethod() {
        for (TournamentPlayer player : tournamentPlayers) {
            float points = 0.0f;
            for (TournamentPlayer opponent : player.getPrevOpponents()) {
                points += opponent.getPoints();
            }
            if (player.hadByeBefore()) {
                points -= 0.5f;
            }
            player.setBuchholzPoints(points);
        }
    }

    private void medianBuchholzMethod() {
        for (TournamentPlayer player : tournamentPlayers) {
            List<TournamentPlayer> opponents = getOrderOpponent(player.getPrevOpponents());
            float points = 0.0f;
            for (int i = 1; i < opponents.size() - 1; i++) {
                points += opponents.get(i).getPoints();
            }
            player.setMedianBuchholzMethod(points);
        }
    }

    private List<TournamentPlayer> getOrderOpponent(List<TournamentPlayer> playerList) {
        Collections.sort(playerList, new Comparator<TournamentPlayer>() {
            @Override
            public int compare(TournamentPlayer o1, TournamentPlayer o2) {
                return Float.compare(o1.getPoints(), o2.getPoints());
            }
        });
        return playerList;
    }

    private void removeMatchesInGroup(int groupNumber) {

        List<TournamentPlayer> lastGroup = groupsPlayers.get(groupNumber);

        Iterator<Match> matchIterator = matchesTmp.listIterator(matchesTmp.size()); //from the end

        while (((ListIterator<Match>) matchIterator).hasPrevious()) {
            Match match = ((ListIterator<Match>) matchIterator).previous();
            for (TournamentPlayer player : lastGroup) {
                if (match.getPlayer1() == player || match.getPlayer2() == player) {
                    removeMatchFromPlayers(match.getPlayer1(), match.getPlayer2());
                    matchIterator.remove();
                    break;
                }
            }
        }

    }

    private void removeMatchFromPlayers(TournamentPlayer player1, TournamentPlayer player2) {
        player1.removeLastMatch();
        player2.removeLastMatch();
    }

    private boolean findOpponent(TournamentPlayer player1, int playersInGroup, int group) {

        //find opponent in the second subgroup
        for (int i = playersInGroup / 2; i < playersInGroup; i++) {
            TournamentPlayer player2 = groupsPlayers.get(group).get(i);

            if (!player2.hasOpponent(currentRound) && !player1.isPlayedTogether(player2) && (player1 != player2)) {
                addMatch(player1, player2);
                return true;
            }
        }

        //find opponent in the first subgroup
        for (int i = 0; i < playersInGroup / 2; i++) {
            TournamentPlayer player2 = groupsPlayers.get(group).get(i);

            if (!player2.hasOpponent(currentRound) && !player1.isPlayedTogether(player2) && (player1 != player2)) {
                addMatch(player1, player2);
                return true;
            }
        }
        return false;
    }


    private void addMatchVersusBye(TournamentPlayer player) {

        TournamentPlayer bye = new TournamentPlayer();
        bye.setName(Constants.BYE);
        bye.setSurname(Constants.EMPTY);
        matchesTmp.add(new Match(currentRound, player, bye));
        player.setBye(true);
        player.setPrevOpponents(bye);
        player.setPrevColors(Colors.NO_COLOR);
    }

    private void addMatch(TournamentPlayer player1, TournamentPlayer player2) {
        if (selectColors(player1, player2)) {
            matchesTmp.add(new Match(currentRound, player1, player2));
            player1.setPrevColors(Colors.WHITE);
            player2.setPrevColors(Colors.BLACK);
        } else {
            matchesTmp.add(new Match(currentRound, player2, player1));
            player1.setPrevColors(Colors.BLACK);
            player2.setPrevColors(Colors.WHITE);
        }

        player1.setPrevOpponents(player2);
        player2.setPrevOpponents(player1);
    }

    private TournamentPlayer findByePlayer() {

        int i = playersNumber - 1;
        while (i >= 0) {
            TournamentPlayer player = tournamentPlayers.get(i);
            if (!player.hadByeBefore()) { //no bye before
                removePlayerFromDrawing(player);
                removeEmptyGroup();
                return player;
            }
            i--;
        }
        return null;
    }

    private void removeEmptyGroup() {
        Iterator<List<TournamentPlayer>> it = groupsPlayers.listIterator(groupsPlayers.size());
        while (((ListIterator<List<TournamentPlayer>>) it).hasPrevious()) {
            List<TournamentPlayer> players = ((ListIterator<List<TournamentPlayer>>) it).previous();
            if (players.isEmpty()) {
                it.remove();
                break;
            }
        }
    }

    private void removePlayerFromDrawing(TournamentPlayer playerToRemove) {
        for (List<TournamentPlayer> list : groupsPlayers) {
            for (TournamentPlayer player : list) {
                if (player == playerToRemove) {
                    list.remove(player);
                    break;
                }
            }
        }
    }

    private void addFirstMatchRound(TournamentPlayer player1, TournamentPlayer player2) {

        Random randomGenerator = new Random();
        if (randomGenerator.nextBoolean()) { //player1 = white

            matchesTmp.add(new Match(currentRound, player1, player2));
            player1.setPrevColors(Colors.WHITE);
            player2.setPrevColors(Colors.BLACK);

        } else { //player1 = black

            matchesTmp.add(new Match(currentRound, player2, player1));
            player1.setPrevColors(Colors.BLACK);
            player2.setPrevColors(Colors.WHITE);
        }


        player1.setPrevOpponents(player2);
        player2.setPrevOpponents(player1);
    }

    private List<List<TournamentPlayer>> prepareGroups() {
        float pointsTemp = tournamentPlayers.get(0).getPoints(); //the best score
        groupsPlayers.clear(); //new drawing

        int currPos = 0;
        List<TournamentPlayer> list = new ArrayList<>();
        for (TournamentPlayer player : tournamentPlayers) {

            boolean adding = true;
            while (player.getPoints() != pointsTemp) {
                pointsTemp -= 0.5f;
                if (adding) {
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

    //return true if player1 has white color, false = black
    private boolean selectColors(TournamentPlayer player1, TournamentPlayer player2) {

        if (player1.countColorInTheRow() > player2.countColorInTheRow()) {
            return (player1.getLastColor().opposite() == Colors.WHITE);
        } else {
            return !(player2.getLastColor().opposite() == Colors.WHITE);
        }
    }


    public void setResult(List<MatchResult> results) {

        List<Match> matchesInGroup = matches.get(currentRound - 1);
        int i = 0;
        for (MatchResult matchResult : results) {
            matchesInGroup.get(i).setMatchResult(matchResult);//matches.get(currentRound - 1).get.setMatchResult(matchResult);
            TournamentPlayer player1 = matchesInGroup.get(i).getPlayer1();
            TournamentPlayer player2 = matchesInGroup.get(i).getPlayer2();

            switch (matchResult) {
                case WHITE_WON: //player1 won
                    player1.addPoints(1);
                    break;
                case BLACK_WON: //player2 won
                    player2.addPoints(1);
                    break;
                case DRAW: //draw
                    player1.addPoints(0.5f);
                    player2.addPoints(0.5f);
                    break;
            }
            i++;
        }

        if (currentRound == roundsNumber) {
            finishedTournament = true;


            if (placeOrder) {
                buchholzMethod();
            } else {
                medianBuchholzMethod();
            }

            sortPlayerAfterTournament();
        } else {
            currentRound++;
        }

    }

    private void sortPlayerDuringTournament() {
        Collections.sort(tournamentPlayers, new Comparator<TournamentPlayer>() {
            @Override
            public int compare(TournamentPlayer o1, TournamentPlayer o2) {
                int c = Float.compare(o2.getPoints(), o1.getPoints()); //descending order
                if (c == 0) {
                    c = Integer.compare(o2.getInternationalRanking(), o1.getInternationalRanking());
                    if (c == 0) {
                        c = Integer.compare(o2.getPolishRanking(), o1.getPolishRanking());
                        if (c == 0) {
                            c = o1.toString().compareTo(o2.toString());
                        }
                    }
                }
                return c;
            }
        });

    }

    private void sortPlayerAfterTournament() {

        Collections.sort(tournamentPlayers, new Comparator<TournamentPlayer>() {
            @Override
            public int compare(TournamentPlayer o1, TournamentPlayer o2) {
                int c = Float.compare(o2.getPoints(), o1.getPoints()); //descending order

                if (c == 0) {
                    if (placeOrder)
                        c = Float.compare(o2.getBuchholzPoints(), o1.getBuchholzPoints());
                    else
                        c = Float.compare(o2.getMedianBuchholzMethod(), o1.getMedianBuchholzMethod());
                }
                return c;
            }
        });
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

    public List<List<Match>> getMatches() {
        return matches;
    }

    public void setMatches(List<List<Match>> matches) {
        this.matches = matches;
    }

    public boolean isEven() {
        return even;
    }

    public void setEven(boolean even) {
        this.even = even;
    }

    public boolean isFinishedTournament() {
        return finishedTournament;
    }

    public void setFinishedTournament(boolean finishedTournament) {
        this.finishedTournament = finishedTournament;
    }

    public boolean getPlaceOrder() {
        return placeOrder;
    }

    public void setPlaceOrder(boolean placeOrder) {
        this.placeOrder = placeOrder;
    }
}
