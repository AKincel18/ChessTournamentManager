package com.example.adam.chesstournamentmanager;

import com.example.adam.chesstournamentmanager.matches.MatchResult;
import com.example.adam.chesstournamentmanager.model.Player;
import com.example.adam.chesstournamentmanager.model.TournamentPlayer;
import com.example.adam.chesstournamentmanager.staticdata.Constants;
import com.example.adam.chesstournamentmanager.swissalgorithm.SwissAlgorithm;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class SwissAlgorithmTest {

    private int roundsNumber;

    private List<TournamentPlayer> tournamentPlayers;

    private int maxNumber = 50; //set max number of rounds

    private int minNumber = 2; //set min number of rounds

    @Before
    public void setUp() {

    }

    private int maxNumberCount(int playersSize) {
        if (playersSize % 2 == 0)
            return playersSize - 1;
        else
            return playersSize;
    }

    private List<Player> initPlayers(int... numberPlayer) {
        List<Player> players = new ArrayList<>();
        int randomNumberPlayers = numberPlayer.length == 0 ? new Random().nextInt(maxNumber) + minNumber : numberPlayer[0];
        for (int i = 1; i <= randomNumberPlayers; i++) {
            Player player = new Player(String.valueOf(i), "", new Random().nextInt(10000) + 1, new Random().nextInt(10000) + 1, new Date());
            players.add(player);
        }
        return players;
    }

    private boolean isRepeatableOpponent(TournamentPlayer player) {
        Set<TournamentPlayer> set = new HashSet<>();

        for (TournamentPlayer opponent : player.getPrevOpponents()) {
            if (!set.add(opponent)) {
                return true;
            }
        }
        return false;
    }

    private MatchResult getRandomMatchResult() {
        Random random = new Random();
        int i = random.nextInt(3);
        switch (i) {
            case 0:
                return MatchResult.WHITE_WON;
            case 1:
                return MatchResult.BLACK_WON;
            case 2:
                return MatchResult.DRAW;
        }
        return null;
    }

    private List<MatchResult> result() {

        List<MatchResult> results = new ArrayList<>();

        int matchNumber;
        if (tournamentPlayers.size() % 2 == 0)
            matchNumber = tournamentPlayers.size() / 2;
        else
            matchNumber = tournamentPlayers.size() / 2 + 1;

        for (int i = 0; i < matchNumber; i++) {
            if (i == matchNumber - 1 && (tournamentPlayers.size() / 2) % 2 == 1)
                results.add(MatchResult.WHITE_WON);
            else {
                MatchResult result = getRandomMatchResult();
                results.add(result);
            }

        }
        return results;

    }

    private void tournament() {

        SwissAlgorithm.getINSTANCE().drawFirstRound();
        List<MatchResult> results = result();
        SwissAlgorithm.getINSTANCE().setResult(results);

        for (int i = 1; i < roundsNumber; i++) {
            SwissAlgorithm.getINSTANCE().drawNextRound();
            results = result();
            SwissAlgorithm.getINSTANCE().setResult(results);
        }
    }

    private boolean byeCheck(TournamentPlayer player) {
        int count = 0;
        for (TournamentPlayer opponent : player.getPrevOpponents()) {
            if (opponent.getName().equals(Constants.BYE)) {
                count++;
                if (count > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    private void initMinPlayersNumber() {

        int playersNumber = 2;
        roundsNumber = maxNumberCount(playersNumber);

        boolean placeOrder = new Random().nextBoolean();
        SwissAlgorithm.initSwissAlgorithm(roundsNumber, placeOrder); //place order is not important
        List<Player> players = initPlayers(playersNumber);
        SwissAlgorithm.getINSTANCE().initTournamentPlayers(players);
        tournamentPlayers = SwissAlgorithm.getINSTANCE().getTournamentPlayers();
    }

    private void initTestRandom() {
        List<Player> players = initPlayers();
        int maxRound = maxNumberCount(players.size());

        roundsNumber = new Random().nextInt(maxRound) + 1;
        boolean placeOrder = new Random().nextBoolean();

        SwissAlgorithm.initSwissAlgorithm(roundsNumber, placeOrder);
        SwissAlgorithm.getINSTANCE().initTournamentPlayers(players);
        tournamentPlayers = SwissAlgorithm.getINSTANCE().getTournamentPlayers();
    }

    private void initTestMaxRoundsNumber() {
        int playersNumber = new Random().nextInt(maxNumber) + minNumber;
        roundsNumber = maxNumberCount(playersNumber);

        boolean placeOrder = new Random().nextBoolean();
        SwissAlgorithm.initSwissAlgorithm(roundsNumber, placeOrder); //place order is not important
        List<Player> players = initPlayers(playersNumber);
        SwissAlgorithm.getINSTANCE().initTournamentPlayers(players);
        tournamentPlayers = SwissAlgorithm.getINSTANCE().getTournamentPlayers();
    }

    @Test
    public void minimumPlayersNumber() {
        initMinPlayersNumber();
        System.out.println(Constants.minimumPlayersNumber);
        System.out.println(Constants.PLAYERS_NUMBER + tournamentPlayers.size());
        System.out.println(Constants.ROUNDS_NUMBER + roundsNumber);
        tournament();

        for (TournamentPlayer player : tournamentPlayers) {
            assertFalse(isRepeatableOpponent(player));
        }
    }

    //random number players and rounds
    @Test
    public void repeatableOpponentRandom() {

        initTestRandom();

        System.out.println(Constants.repeatableOpponentRandom);
        System.out.println(Constants.PLAYERS_NUMBER + tournamentPlayers.size());
        System.out.println(Constants.ROUNDS_NUMBER + roundsNumber);
        tournament();

        for (TournamentPlayer player : tournamentPlayers) {
            assertFalse(isRepeatableOpponent(player));
        }
    }

    @Test
    public void repeatableOpponentMaxRoundsNumber() {

        initTestMaxRoundsNumber();

        System.out.println(Constants.repeatableOpponentMaxRoundsNumber);
        System.out.println(Constants.PLAYERS_NUMBER + tournamentPlayers.size());
        System.out.println(Constants.ROUNDS_NUMBER + roundsNumber);


        tournament();

        for (TournamentPlayer player : tournamentPlayers) {
            assertFalse(isRepeatableOpponent(player));
        }


    }


    @Test
    public void checkByeRandom() {

        initTestRandom();

        System.out.println(Constants.checkByeRandom);
        System.out.println(Constants.PLAYERS_NUMBER + tournamentPlayers.size());
        System.out.println(Constants.ROUNDS_NUMBER + roundsNumber);
        tournament();


        for (TournamentPlayer player : tournamentPlayers) {
            assertTrue(byeCheck(player));
        }
    }

    @Test
    public void checkByeMaxRoundsNumber() {

        initTestMaxRoundsNumber();

        System.out.println(Constants.checkByeMaxRoundsNumber);
        System.out.println(Constants.PLAYERS_NUMBER + tournamentPlayers.size());
        System.out.println(Constants.ROUNDS_NUMBER + roundsNumber);

        tournament();

        for (TournamentPlayer player : tournamentPlayers) {
            assertTrue(byeCheck(player));
        }
    }

    @Test
    public void correctFinalOrderRandom() {

        initTestRandom();

        System.out.println(Constants.correctFinalOrderRandom);
        System.out.println(Constants.PLAYERS_NUMBER + tournamentPlayers.size());
        System.out.println(Constants.ROUNDS_NUMBER + roundsNumber);

        tournament();

        for (int i = 0; i < tournamentPlayers.size(); i++) {
            if (i != tournamentPlayers.size() - 1) {
                assertTrue(tournamentPlayers.get(i).getPoints() >= tournamentPlayers.get(i + 1).getPoints());
            }
        }

    }

    @Test
    public void correctFinalOrderMaxRoundNumber() {

        initTestMaxRoundsNumber();

        System.out.println(Constants.correctFinalOrderMaxRoundNumber);
        System.out.println(Constants.PLAYERS_NUMBER + tournamentPlayers.size());
        System.out.println(Constants.ROUNDS_NUMBER + roundsNumber);

        tournament();

        for (int i = 0; i < tournamentPlayers.size(); i++) {
            if (i != tournamentPlayers.size() - 1) {
                assertTrue(tournamentPlayers.get(i).getPoints() >= tournamentPlayers.get(i + 1).getPoints());
            }
        }

    }

    @After
    public void tearDown() {
        SwissAlgorithm.resetTournament();
        roundsNumber = 0;
        tournamentPlayers.clear();
        System.out.println();
    }


}
