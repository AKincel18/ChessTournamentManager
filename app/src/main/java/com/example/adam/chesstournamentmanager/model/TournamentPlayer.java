package com.example.adam.chesstournamentmanager.model;


import java.util.ArrayList;
import java.util.List;


public class TournamentPlayer extends Players {

    private float points;

    private List<TournamentPlayer> prevOponents = new ArrayList<>();

    private List<Colors> prevColors = new ArrayList<>();

    private boolean bye;

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

    public boolean isBye() {
        return bye;
    }

    public void setBye(boolean bye) {
        this.bye = bye;
    }

    public boolean isGoodOponent(Players player){
        for (TournamentPlayer tournamentPlayer : prevOponents){
            if (tournamentPlayer.getPlayer() == player)
                return false;
        }
        return true;
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
}
