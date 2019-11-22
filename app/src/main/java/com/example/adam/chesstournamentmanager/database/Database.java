package com.example.adam.chesstournamentmanager.database;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.adam.chesstournamentmanager.model.Player;
import com.example.adam.chesstournamentmanager.staticdata.Constans;

@android.arch.persistence.room.Database(entities = Player.class, version = 5, exportSchema = false)
public abstract class Database extends RoomDatabase {

    private static Database instance;

    public static Database getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    Database.class, Constans.DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract PlayersDao playersDao();
}
