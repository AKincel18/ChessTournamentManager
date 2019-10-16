package com.example.adam.chesstournamentmanager.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.adam.chesstournamentmanager.model.Players;

import java.util.List;

@Dao
public interface PlayersDao {

    @Query("SELECT * FROM PLAYERS")
    List<Players> getAllPlayers();

    @Insert
    void insertPlayer(Players players);

}
