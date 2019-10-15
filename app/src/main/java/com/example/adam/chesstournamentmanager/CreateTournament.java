package com.example.adam.chesstournamentmanager;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import database.DatabaseHelper;
import staticdata.Constans;

public class CreateTournament extends AppCompatActivity {

    ArrayList<String> selectedAvailablePlayers = new ArrayList<>();
    ArrayList<String> selectedChosenPlayers = new ArrayList<>();

    ListView chosenPlayerListView;
    ListView allPlayersListView;

    ArrayList<String> allPlayers = new ArrayList<>();
    ArrayList<String> chosenPlayers = new ArrayList<>();

    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tournament);
        chosenPlayerListView = findViewById(R.id.chosenPlayersList);
        allPlayersListView = findViewById(R.id.playersList);
        myDb = new DatabaseHelper(this); //TODO no create new database instance after launch activity

        initPlayers();



        Intent i = getIntent();
        if (i.getStringArrayListExtra("allPlayers") !=  null){
            allPlayers = i.getStringArrayListExtra("allPlayers");
            Toast.makeText(this, Constans.ADDED_NEW_PLAYER, Toast.LENGTH_LONG).show(); //TODO string from string.xml (not easy, maybe not possible :/)
        }

        initListView(allPlayersListView, allPlayers, selectedAvailablePlayers); //TODO chosen player disappear after add new player from popup
        initListView(chosenPlayerListView, chosenPlayers, selectedChosenPlayers);

    }

    private void initPlayers(){

        Cursor result = myDb.getData();
        while (result.moveToNext()){
            StringBuffer name = new StringBuffer();
            name.append(result.getString(1) + " ");
            name.append(result.getString(2));
            allPlayers.add(name.toString());
        }
    }

    public void moveToChosenPlayers(View view){ //'->' button
        movement(chosenPlayers, allPlayers, selectedAvailablePlayers);
    }


    public void moveToAvailablePlayers(View view){ //'<-' button
        movement(allPlayers, chosenPlayers, selectedChosenPlayers);
    }

    private void movement(ArrayList<String> add, ArrayList<String> remove, ArrayList<String> selected){
        if (selected.isEmpty())
            Toast.makeText(this, Constans.NOTHING_SELECTED, Toast.LENGTH_LONG).show();
        else {
            add.addAll(selected);
            remove.removeAll(selected);
            selected.clear();

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_players, R.id.simpleCheckedTextView, allPlayers);
            allPlayersListView.setAdapter(adapter);
            adapter = new ArrayAdapter<>(this, R.layout.list_players, R.id.simpleCheckedTextView, chosenPlayers);
            chosenPlayerListView.setAdapter(adapter);
        }
    }

    private void initListView(ListView listView, ArrayList<String> playersList, final ArrayList<String> selected){

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_players, R.id.simpleCheckedTextView, playersList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedPlayer = ((TextView)view).getText().toString();
                if (selected.contains(selectedPlayer))
                    selected.remove(selectedPlayer); //uncheck player
                else
                    selected.add(selectedPlayer);
            }
        });

    }

    public void addNewPlayer(View view){
        Intent i = new Intent(getApplicationContext(), AddNewPlayer.class);
        i.putExtra("allPlayers", allPlayers);
        startActivity(i);
    }
}
