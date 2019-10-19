package com.example.adam.chesstournamentmanager.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.adam.chesstournamentmanager.model.Players;

import java.util.List;

@Dao
public interface PlayersDao {

    @Query("SELECT * FROM PLAYERS ORDER BY surname, name")
    List<Players> getAllPlayers();

    @Query("SELECT * FROM PLAYERS as p WHERE p.name = :name AND p.surname = :surname")
    Players findPlayer(String name, String surname);
/*

    @Query("DELETE FROM PLAYERS")
    void delete();
*/

    @Insert
    void insertPlayer(Players players);

}
