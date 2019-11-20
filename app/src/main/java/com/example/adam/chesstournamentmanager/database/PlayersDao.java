package com.example.adam.chesstournamentmanager.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.adam.chesstournamentmanager.model.Players;

import java.util.List;

@Dao
public interface PlayersDao {

    @Query("SELECT * FROM PLAYERS ORDER BY surname, name")
    List<Players> getAllPlayers();

    @Insert
    void insertPlayer(Players players);

    @Delete
    void removePlayer(List<Players> players);

    @Update
    void updatePlayer(Players players);

}
