package com.example.adam.chesstournamentmanager.model;



import java.util.ArrayList;
import java.util.List;


public class TournamentPlayer extends Players {

    private float points;

    private List<TournamentPlayer> prevOpponents = new ArrayList<>();

    private List<Colors> prevColors = new ArrayList<>();

    private float buchholzPoints;

    private float medianBuchholzMethod;

    private boolean bye;

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

    public List<TournamentPlayer> getPrevOpponents() {
        return prevOpponents;
    }

    public void setPrevOpponents(TournamentPlayer prevOpponents) {
        this.prevOpponents.add(prevOpponents);
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

    public float getBuchholzPoints() {
        return buchholzPoints;
    }

    public void setBuchholzPoints(float buchholzPoints) {
        this.buchholzPoints = buchholzPoints;
    }

    public float getMedianBuchholzMethod() {
        return medianBuchholzMethod;
    }

    public void setMedianBuchholzMethod(float medianBuchholzMethod) {
        this.medianBuchholzMethod = medianBuchholzMethod;
    }

    public void removeLastMatch(){
        prevOpponents.remove(prevOpponents.size() - 1);
        prevColors.remove(prevColors.size() - 1);
    }

    public boolean isPlayedTogether(TournamentPlayer player){
        for (TournamentPlayer tournamentPlayer : prevOpponents){
            if (tournamentPlayer == player)
                return true;
        }
        return false;
    }

    public boolean hasOpponent(int currentRound){
        return currentRound == prevOpponents.size();
    }


    public String writeOpponent(){
        String tmp = "";
        for (TournamentPlayer player : prevOpponents){
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

}
