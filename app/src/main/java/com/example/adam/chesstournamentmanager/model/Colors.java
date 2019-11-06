package com.example.adam.chesstournamentmanager.model;

public enum Colors {
    WHITE, BLACK, NO_COLOR;

    public Colors opposite(){
        switch (this) {
            case WHITE:
                return BLACK;
            case BLACK:
                return WHITE;
            case NO_COLOR:
                return null;

        }
        return null;
        //return Colors.values()[(this.ordinal() + 1) % 2]; //return opposite value
    }
}
