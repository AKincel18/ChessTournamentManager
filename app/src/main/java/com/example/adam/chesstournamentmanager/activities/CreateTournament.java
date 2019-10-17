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

    private ArrayList<String> selectedAvailablePlayers = new ArrayList<>();
    private ArrayList<String> selectedChosenPlayers = new ArrayList<>();

    private ListView chosenPlayerListView;
    private ListView allPlayersListView;

    private ArrayList<String> availablePlayers = new ArrayList<>();
    static private ArrayList<String> chosenPlayers = new ArrayList<>();

    static boolean isInitPlayers = true;

    private Database database;

    //TODO code refator -> date, hardcode string, warnings -> informations
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tournament);
        chosenPlayerListView = findViewById(R.id.chosenPlayersList);
        allPlayersListView = findViewById(R.id.playersList);

        database = Database.getInstance(this);

        Intent i = getIntent();
        if (i.getStringArrayListExtra("availablePlayers") !=  null){
            availablePlayers = i.getStringArrayListExtra("availablePlayers");
            Toast.makeText(this, Constans.ADDED_NEW_PLAYER, Toast.LENGTH_LONG).show(); //TODO string from string.xml (not easy, maybe not possible :/)
        }


        initListView(chosenPlayerListView, chosenPlayers, selectedChosenPlayers);

        if (isInitPlayers)
            initPlayers();

        initListView(allPlayersListView, availablePlayers, selectedAvailablePlayers);

   }

    @Override
    protected void onPause(){
        super.onPause();
        isInitPlayers = false;
    }
    private void initPlayers(){

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                for (Players p : database.playersDao().getAllPlayers()) {
                    availablePlayers.add(p.getName() + " " + p.getSurname());
                    System.out.println(p.getName() + " " + p.getSurname() + " " + p.getDateOfBirth());
                }
            }
        });
    }

    public void moveToChosenPlayers(View view){ //'->' button
        movement(chosenPlayers, availablePlayers, selectedAvailablePlayers);
    }


    public void moveToAvailablePlayers(View view){ //'<-' button
        movement(availablePlayers, chosenPlayers, selectedChosenPlayers);
    }

    private void movement(ArrayList<String> add, ArrayList<String> remove, ArrayList<String> selected){
        if (selected.isEmpty())
            Toast.makeText(this, Constans.NOTHING_SELECTED, Toast.LENGTH_LONG).show();
        else {
            add.addAll(selected);
            remove.removeAll(selected);
            selected.clear();

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_players, R.id.simpleCheckedTextView, availablePlayers);
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
        i.putExtra("availablePlayers", availablePlayers);
        startActivity(i);
    }
}
