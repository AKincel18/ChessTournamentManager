package com.example.adam.chesstournamentmanager;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import staticdata.Constans;

public class CreateTournament extends AppCompatActivity {

    ArrayList<String> selectedAvailablePlayers = new ArrayList<>();
    ArrayList<String> selectedChosenPlayers = new ArrayList<>();

    ListView chosenPlayerListView;
    ListView allPlayersListView;

    ArrayList<String> allPlayers = new ArrayList<>();
    ArrayList<String> chosenPlayers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tournament);
        chosenPlayerListView = findViewById(R.id.chosenPlayersList);
        allPlayersListView = findViewById(R.id.playersList);
        allPlayers.add("Robert Lewandowski"); //TODO change to data from database
        allPlayers.add("Arkadiusz Milik");
        allPlayers.add("Krzysztof Piatek");
        allPlayers.add("Dawid Kownacki");
        initListView(allPlayersListView, allPlayers, selectedAvailablePlayers);
        initListView(chosenPlayerListView, chosenPlayers, selectedChosenPlayers);

        Intent i = getIntent();
        if (i.getStringExtra("data") !=  null){
            allPlayers.add(i.getStringExtra("data"));
            Toast.makeText(this, Constans.ADDED_NEW_PLAYER, Toast.LENGTH_LONG).show(); //TODO string from string.xml (not easy, maybe not possible :/)
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
        startActivity(i);
    }
}
