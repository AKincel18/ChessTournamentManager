package com.example.adam.chesstournamentmanager.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.adam.chesstournamentmanager.R;
import com.example.adam.chesstournamentmanager.model.Players;
import com.example.adam.chesstournamentmanager.staticdata.Constans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
                        //Collections.sort(players);

                        Collections.sort(players, new Comparator<Players>() {
                            @Override
                            public int compare(Players o1, Players o2) {
                                int c = o1.getSurname().compareTo(o2.getSurname());

                                if (c == 0)
                                    return o1.getName().compareTo(o2.getName());
                                return c;
                            }
                        });
                        System.out.println(Constans.ALPHABETICAL_ORDER);


                        initListView(listView, players);

                        break;
                    case (Constans.POLISH_RANKING_ORDER):
                        System.out.println(Constans.POLISH_RANKING_ORDER);
                        //Todo sorting by polish ranking
                        break;
                    case (Constans.INTERNATIONAL_RANKING_ORDER):
                        System.out.println(Constans.INTERNATIONAL_RANKING_ORDER);
                        //todo sorting by international ranking
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        initListView(listView, players);
    }

    private void initListView(ListView listView, List<Players> playersList){

        ArrayAdapter<Players> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, playersList);
        listView.setAdapter(adapter);

    }
}
