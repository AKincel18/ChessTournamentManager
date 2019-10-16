package com.example.adam.chesstournamentmanager.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.concurrent.Executors;

import com.example.adam.chesstournamentmanager.R;
import com.example.adam.chesstournamentmanager.database.Database;
import com.example.adam.chesstournamentmanager.model.Players;
import com.example.adam.chesstournamentmanager.staticdata.Constans;

public class CreateTournament extends AppCompatActivity {

    ArrayList<String> selectedAvailablePlayers = new ArrayList<>();
    ArrayList<String> selectedChosenPlayers = new ArrayList<>();

    ListView chosenPlayerListView;
    ListView allPlayersListView;

    ArrayList<String> allPlayers = new ArrayList<>();
    ArrayList<String> chosenPlayers = new ArrayList<>();

    //DatabaseHelper myDb;

    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tournament);
        chosenPlayerListView = findViewById(R.id.chosenPlayersList);
        allPlayersListView = findViewById(R.id.playersList);
        //myDb = new DatabaseHelper(this); //TODO no create new database instance after launch activity
        database = Database.getInstance(this);

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

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                for (Players p : database.playersDao().getAllPlayers())
                    allPlayers.add(p.getName() + " " + p.getSurname()); //TODO write two times to listview
            }
        });
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
