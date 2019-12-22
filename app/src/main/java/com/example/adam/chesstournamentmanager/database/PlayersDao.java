package com.example.adam.chesstournamentmanager.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.adam.chesstournamentmanager.model.Player;

import java.util.List;

@Dao
public interface PlayersDao {

    @Query("SELECT * FROM Players ORDER BY surname, name")
    List<Player> getAllPlayers();

    @Insert
    void insertPlayer(Player player);

    @Delete
    void removePlayer(List<Player> players);

    @Update
    void updatePlayer(Player player);

}


