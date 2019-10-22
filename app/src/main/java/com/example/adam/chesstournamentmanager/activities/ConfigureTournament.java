package com.example.adam.chesstournamentmanager.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.adam.chesstournamentmanager.R;
import com.example.adam.chesstournamentmanager.model.Players;
import com.example.adam.chesstournamentmanager.staticdata.Constans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Formatter;
import java.util.List;

public class ConfigureTournament extends AppCompatActivity {

    private ArrayList<Players> players;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_configure_tournament);

        TextView countOfPlayersTextView = findViewById(R.id.countOfPlayersTextView);
        Intent i = getIntent();
        players = (ArrayList<Players>) i.getSerializableExtra("players");
        countOfPlayersTextView.setText(String.valueOf(players.size()));

        listView = findViewById(R.id.playersListView);
        Spinner spinner = findViewById(R.id.pairingTypeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.pairing, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (parent.getSelectedItem().toString()){
                    case (Constans.ALPHABETICAL_ORDER):
                        comparatorBySurnameName();
                        initListViewAlphabetical(listView, players);
                        break;

                    case (Constans.POLISH_RANKING_ORDER):
                        comparatorByPolishRanking();
                        initListViewByRanking(true);
                        break;

                    case (Constans.INTERNATIONAL_RANKING_ORDER):
                        comparatorByInternationalRanking();
                        initListViewByRanking(false);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        startTournament();

    }


    private void initListViewAlphabetical(ListView listView, List<Players> playersList){

        ArrayAdapter<Players> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, playersList);
        listView.setAdapter(adapter);

    }

    private void initListViewByRanking(boolean i){
        ArrayList<String> strings;
        if (i)
            strings = polishRanking();
        else
            strings = internationalRanking();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, strings);
        listView.setAdapter(adapter);
    }

    //TODO improve formatting in viewlist
    //TODO two the same functions -> change to one :)
    private ArrayList<String> polishRanking(){
        ArrayList<String> s = new ArrayList<>();
        for (Players p : players) {
            Formatter fmt = new Formatter();
            if (p.getPolishRanking() != Integer.MAX_VALUE){
                fmt.format("%20s %20s %15d", p.getSurname(), p.getName(), p.getPolishRanking());
                s.add(fmt.toString());
            }
            else{
                fmt.format("%20s %20s", p.getSurname(), p.getName());
                s.add(fmt.toString());
            }


        }
        return s;

    }

    private ArrayList<String> internationalRanking(){

        ArrayList<String> s = new ArrayList<>();
        for (Players p : players) {
            Formatter fmt = new Formatter();
            if (p.getInternationalRanking() != Integer.MAX_VALUE){
                fmt.format("%20s %20s %15d", p.getSurname(), p.getName(), p.getInternationalRanking());
                s.add(fmt.toString());
            }
            else{
                fmt.format("%20s %20s", p.getSurname(), p.getName());
                s.add(fmt.toString());
            }

        }
        return s;
    }

    private void comparatorBySurnameName(){

        Collections.sort(players, new Comparator<Players>() {
            @Override
            public int compare(Players o1, Players o2) {
                int c = o1.getSurname().compareTo(o2.getSurname());

                if (c == 0)
                    return o1.getName().compareTo(o2.getName());
                return c;
            }
        });

    }

    private void comparatorByPolishRanking(){
        Collections.sort(players, new Comparator<Players>() {
            @Override
            public int compare(Players o1, Players o2) {
                int c = Integer.compare(o1.getPolishRanking(), o2.getPolishRanking());
                if (c == 0)
                    c = o1.getSurname().compareTo(o2.getSurname());
                if (c == 0)
                    return o1.getName().compareTo(o2.getName());

                return c;

            }
        });
    }

    private void comparatorByInternationalRanking(){
        Collections.sort(players, new Comparator<Players>() {
            @Override
            public int compare(Players o1, Players o2) {
                int c = Integer.compare(o1.getInternationalRanking(), o2.getInternationalRanking());
                if (c == 0)
                    c = o1.getSurname().compareTo(o2.getSurname());
                if (c == 0)
                    return o1.getName().compareTo(o2.getName());
                return c;
            }
        });
    }


    private void startTournament() {
        final EditText roundsNumber = findViewById(R.id.roundsNumber);
        Button startTournament = findViewById(R.id.startTournamentButton);
        startTournament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), TournamentProcess.class);
                i.putExtra("roundsNumber", Integer.valueOf(roundsNumber.getText().toString()));
                startActivity(i);
            }
        });


    }
}
