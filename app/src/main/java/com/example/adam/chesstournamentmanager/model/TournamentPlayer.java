package com.example.adam.chesstournamentmanager.model;


import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class TournamentPlayer extends Players {

    private float points;

    private List<TournamentPlayer> prevOponents = new ArrayList<>();

    private List<Colors> prevColors = new ArrayList<>();

    private boolean bye;

    private boolean fallIntoLowerGroup;

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

    public boolean isFallIntoLowerGroup() {
        return fallIntoLowerGroup;
    }

    public void setFallIntoLowerGroup(boolean fallIntoLowerGroup) {
        this.fallIntoLowerGroup = fallIntoLowerGroup;
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



/*    public Colors expectedColor(){
        Colors color;
        if ((color = prevColors.get(prevColors.size() - 1)) == prevColors.get(prevColors.size() - 2)){
            return color.opposite();
        }
        return null;
    }*/
        public Colors expectedColor(){
            Colors color = prevColors.get(prevColors.size() - 1);
            return color.opposite();
        }

        public boolean isColorRepatedTwoLastTimes(){
            if (prevColors.size() > 2){
                return prevColors.get(prevColors.size() - 1) == prevColors.get(prevColors.size() - 2);
            }
            else
                return false;
        }
}
