package com.example.adam.chesstournamentmanager.database;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.adam.chesstournamentmanager.model.Players;

@android.arch.persistence.room.Database(entities = Players.class, version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {

    private static final String DATABASE_NAME = "ChessTournamentManager.db";
    private static Database instance;

    public static Database getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    Database.class, Database.DATABASE_NAME)
                    .build();
        }
        return instance;
    }

    public abstract PlayersDao playersDao();
}
