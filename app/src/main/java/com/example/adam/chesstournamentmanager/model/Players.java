package com.example.adam.chesstournamentmanager.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.Nullable;

import com.example.adam.chesstournamentmanager.database.DateConverter;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "players")
public class Players implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "surname")
    private String surname;

    @ColumnInfo(name = "polish_ranking")
    @Nullable
    private int polishRanking;

    @ColumnInfo(name = "international_ranking")
    @Nullable
    private int internationalRanking;

    @ColumnInfo(name = "date_of_birth")
    @TypeConverters(DateConverter.class)
    private Date dateOfBirth;


    @Ignore
    public Players() {
    }

    @Ignore
    public Players(Players player) {
        this.name = player.getName();
        this.surname = player.getSurname();
        this.polishRanking = player.getPolishRanking();
        this.internationalRanking = player.getInternationalRanking();
        this.dateOfBirth = player.getDateOfBirth();
    }


    public Players(String name, String surname, Date dateOfBirth) {
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
    }

    @Ignore
    public Players(String name, String surname, int polishRanking, int internationalRanking, Date dateOfBirth) {
        this.name = name;
        this.surname = surname;
        this.polishRanking = polishRanking;
        this.internationalRanking = internationalRanking;
        this.dateOfBirth = dateOfBirth;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getPolishRanking() {
        return polishRanking;
    }

    public void setPolishRanking(int polishRanking) {
        this.polishRanking = polishRanking;
    }

    public int getInternationalRanking() {
        return internationalRanking;
    }

    public void setInternationalRanking(int internationalRanking) {
        this.internationalRanking = internationalRanking;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }


    @Override
    public String toString() {
        return surname + " " + name;
    }
}
