package com.example.adam.chesstournamentmanager.model;



import java.util.ArrayList;
import java.util.List;


public class TournamentPlayer extends Players {

    private float points;

    private List<TournamentPlayer> prevOponents = new ArrayList<>();

    private List<Colors> prevColors = new ArrayList<>();

    private boolean bye;

    private boolean fallIntoLastGroup;

    private int countGroup; //o ile musi isc do gory ze swojej grupy

    private int countFallIntoTheLastGroup; //ile razy spadł do najnizej grupy: jesli raz to lec do wyzszej, a jesli dwa to lec do 2 wyzszej

    private boolean upper;

    public TournamentPlayer() {
    }

    public TournamentPlayer(Players player) {
        super(player);
    }

    public float getPoints() {
        return points;
    }


    public void addPoints(float points) {
        this.points += points;
    }

    public List<TournamentPlayer> getPrevOponents() {
        return prevOponents;
    }

    public void setPrevOponents(TournamentPlayer prevOponents) {
        this.prevOponents.add(prevOponents);
    }

    public List<Colors> getPrevColors() {
        return prevColors;
    }

    public Colors getLastColor(){
        return prevColors.get(prevColors.size() - 1);
    }

    public void setPrevColors(Colors prevColors) {
        this.prevColors.add(prevColors);
    }

    public boolean hadByeBefore() {
        return bye;
    }

    public void setBye(boolean bye) {
        this.bye = bye;
    }

    public boolean isUpper() {
        return upper;
    }

    public void setUpper(boolean upper) {
        this.upper = upper;
    }


    /*

    public boolean isFallIntoLastGroup() {
        return fallIntoLastGroup;
    }

    public void setFallIntoLastGroup(boolean fallIntoLastGroup) {
        this.fallIntoLastGroup = fallIntoLastGroup;
    }
*/


    public int getCountGroup() {
        return countGroup;
    }

    public void increaseGroup() {
        this.countGroup++;
    }

    public void setCountGroup(int countGroup){
        this.countGroup = countGroup;
    }

    public int getCountFallIntoTheLastGroup() {
        return countFallIntoTheLastGroup;
    }

    public void increaseFallIntoTheLastGroup(){
        this.countFallIntoTheLastGroup++;
    }
    public void setCountFallIntoTheLastGroup(int countFallIntoTheLastGroup) {
        this.countFallIntoTheLastGroup = countFallIntoTheLastGroup;
    }

    public void removeLastMatch(){
        prevOponents.remove(prevOponents.size() - 1);
        prevColors.remove(prevColors.size() - 1);
    }

    public boolean isPlayedTogether(TournamentPlayer player){
        for (TournamentPlayer tournamentPlayer : prevOponents){
            if (tournamentPlayer == player)
                return true;
        }
        return false;
    }

    public boolean hasOpponent(int currentRound){
        return currentRound == prevOponents.size();
    }


    public String writeOpponent(){
        String tmp = "";
        for (TournamentPlayer player : prevOponents){
            tmp +=  (player.getName() + " ");
        }
        return tmp;
    }

    public String writeColors(){
        String tmp = "";
        for (Colors colors : prevColors){
            tmp += (colors.name() + " ");
        }
        return tmp;
    }

/*
        public Colors expectedColor(){
            int round = prevColors.size() - 1;
            Colors color = prevColors.get(round);
            if (round == 1)
                return null;
            if (color == Colors.NO_COLOR)
                color = prevColors.get(round);
            return color.opposite();
        }*/

        public int countColorInTheRow(){
            if (prevColors.size() == 1)
                return 1;
            int counter = 1;
            for (int i=prevColors.size() - 1; i >= 1; i--){
                if (prevColors.get(i) != Colors.NO_COLOR && prevColors.get(i) == prevColors.get(i - 1)){
                    counter++;
                }else
                    return counter;
            }
            return counter;
        }


    public List<Integer> colorsCount(){
        List<Integer> list = new ArrayList<>();
        int black = 0;
        int white = 0;
        for (Colors color : prevColors){
            if (color == Colors.WHITE)
                white++;
            else if (color == Colors.BLACK)
                black++;
        }
        list.add(black);
        list.add(white);
        return list;
    }


        public boolean isColorRepatedTwoLastTimes(){
            if (prevColors.size() > 2){
                return prevColors.get(prevColors.size() - 1) == prevColors.get(prevColors.size() - 2);
            }
            else
                return false;
        }
}
