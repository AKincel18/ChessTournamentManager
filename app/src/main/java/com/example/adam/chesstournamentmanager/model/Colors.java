package com.example.adam.chesstournamentmanager.model;

public enum Colors {
    WHITE, BLACK, NO_COLOR;

    public Colors opposite(){
        return Colors.values()[(this.ordinal() + 1) % 2]; //return opposite value
    }
}
