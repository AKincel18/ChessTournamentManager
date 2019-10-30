package com.example.adam.chesstournamentmanager.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.adam.chesstournamentmanager.R;
import com.example.adam.chesstournamentmanager.model.Players;
import com.example.adam.chesstournamentmanager.staticdata.Constans;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Formatter;

public class ConfigureTournament extends AppCompatActivity {

    private ArrayList<Players> players;
    private ListView listView;
    private Switch switch1;
    private String order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_tournament);

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
                switch (order = parent.getSelectedItem().toString()){
                    case (Constans.ALPHABETICAL_ORDER):
                        comparatorBySurnameName();
                        initListView();
                        break;

                    case (Constans.POLISH_RANKING_ORDER):
                        comparatorByPolishRanking();
                        polishRanking();
                        break;

                    case (Constans.INTERNATIONAL_RANKING_ORDER):
                        comparatorByInternationalRanking();
                        internationalRanking();
                        break;

                    case (Constans.RANDOM_ORDER):
                        Collections.shuffle(players);
                        initListView();
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        switch1 = findViewById(R.id.countOfNumberSwitch);

        int optimalCountOfRunds =(int)Math.ceil(Math.log(players.size()) / Math.log(2)); // ceil(log2(ilość_graczy)),  log2 = (Math.log(x) / Math.log(2));
        switchImplementation(optimalCountOfRunds);
        startTournament(optimalCountOfRunds);

    }


    private void initListView(){

        ArrayAdapter<Players> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, players);
        listView.setAdapter(adapter);

    }

    private void initListViewByRanking(ArrayList<String> strings){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, strings);
        listView.setAdapter(adapter);
    }

    //TODO improve formatting in viewlist
    //TODO two the same functions -> change to one :)
    private void polishRanking(){
        ArrayList<String> s = new ArrayList<>();
        for (Players p : players) {
            Formatter fmt = new Formatter();
            if (p.getPolishRanking() != Integer.MAX_VALUE){
                fmt.format("%1s %20s %15d", p.getSurname(), p.getName(), p.getPolishRanking());
                s.add(fmt.toString());
            }
            else{
                fmt.format("%1s %20s", p.getSurname(), p.getName());
                s.add(fmt.toString());
            }
        initListViewByRanking(s);

        }

    }

    private void internationalRanking(){

        ArrayList<String> s = new ArrayList<>();
        for (Players p : players) {
            Formatter fmt = new Formatter();
            if (p.getInternationalRanking() != Integer.MAX_VALUE){
                fmt.format("%1s %20s %15d", p.getSurname(), p.getName(), p.getInternationalRanking());
                s.add(fmt.toString());
            }
            else{
                fmt.format("%1s %20s", p.getSurname(), p.getName());
                s.add(fmt.toString());
            }
        initListViewByRanking(s);
        }
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



    private void startTournament(final int optimalCountOfRunds) {
        final EditText roundsNumber = findViewById(R.id.roundsNumber);
        Button startTournament = findViewById(R.id.startTournamentButton);
        startTournament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), TournamentProcess.class);
                if (switch1.isChecked()){
                    i.putExtra("roundsNumber", Integer.valueOf(roundsNumber.getText().toString())); //TODO java.lang.NumberFormatException: For input string: ""
                }
                else {
                    i.putExtra("roundsNumber", Integer.valueOf(optimalCountOfRunds));
                }

                i.putExtra("players", players);
                i.putExtra("order", order);
                startActivity(i);
            }
        });


    }

    private void switchImplementation(int optimalCountOfRunds){
        final TextView textView = findViewById(R.id.autoRoundsNumber);
        final EditText editText = findViewById(R.id.roundsNumber);

        textView.setText(getString(R.string.autoCountOfRounds, optimalCountOfRunds));



        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    editText.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.INVISIBLE);
                }
                else{
                    editText.setVisibility(View.INVISIBLE);
                    textView.setVisibility(View.VISIBLE);

                }
            }
        });
    }
}
