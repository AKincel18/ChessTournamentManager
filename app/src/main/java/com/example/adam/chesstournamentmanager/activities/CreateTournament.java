package com.example.adam.chesstournamentmanager.activities;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.concurrent.Executors;

import com.example.adam.chesstournamentmanager.R;
import com.example.adam.chesstournamentmanager.database.Database;
import com.example.adam.chesstournamentmanager.model.Players;
import com.example.adam.chesstournamentmanager.staticdata.dialogbox.GeneralDialogFragment;

public class CreateTournament extends AppCompatActivity implements GeneralDialogFragment.OnDialogFragmentClickListener {

    private ArrayList<Players> selectedAvailablePlayers = new ArrayList<>();
    private ArrayList<Players> selectedChosenPlayers = new ArrayList<>();

    private ListView chosenPlayerListView;
    private ListView allPlayersListView;

    static private ArrayList<Players> availablePlayers = new ArrayList<>();
    static private ArrayList<Players> chosenPlayers = new ArrayList<>();

    static boolean isInitPlayers = true;

    private Database database;

    private Dialog dialog;

    private boolean chooseDialogBox;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tournament);
        chosenPlayerListView = findViewById(R.id.chosen_players_list_view);
        allPlayersListView = findViewById(R.id.available_players_list_view);

        database = Database.getInstance(this);

        Intent i = getIntent();
        if (i.getSerializableExtra(getString(R.string.available_players)) !=  null){
            availablePlayers =(ArrayList<Players>)i.getSerializableExtra(getString(R.string.available_players));
            Toast.makeText(this, getString(R.string.added_player), Toast.LENGTH_LONG).show();
        }


        if (isInitPlayers) {
            initPlayers(); //fetch players from database
            try {

                Thread.sleep(100);//wait to fetching data from database

            }catch (Exception e){
                e.printStackTrace();
            }
        }


        //buttons
        moveToChosenPlayers();
        moveToAvailablePlayers();
        addNewPlayer();
        configureTournament();
        aboutPlayer();
        removePlayer();

        selectedAll(R.id.select_all_checkbox, allPlayersListView, selectedAvailablePlayers);
        selectedAll(R.id.select_all_checkbox2, chosenPlayerListView, selectedChosenPlayers);

        initListView(chosenPlayerListView, chosenPlayers, selectedChosenPlayers);
        initListView(allPlayersListView, availablePlayers, selectedAvailablePlayers);


        dialog = new Dialog(this);


   }



    @Override
    protected void onPause(){
        super.onPause();
        isInitPlayers = false;
    }



    private void initPlayers(){

        Executors.newSingleThreadExecutor().execute(    new Runnable() {
            @Override
            public void run() {
                //availablePlayers.addAll(database.playersDao().getAllPlayers());
                availablePlayers.addAll(database.playersDao().getAllPlayers());
            }
        });
    }

    public void moveToChosenPlayers(){ //'->' button
        Button nextButton = findViewById(R.id.move_to_chosen_players_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movement(chosenPlayers, availablePlayers, selectedAvailablePlayers);
                CheckBox checkBox = findViewById(R.id.select_all_checkbox);
                checkBox.setChecked(false);
            }
        });

    }


    public void moveToAvailablePlayers(){ //'<-' button
        Button backButton = findViewById(R.id.move_to_available_players_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movement(availablePlayers, chosenPlayers, selectedChosenPlayers);
                CheckBox checkBox = findViewById(R.id.select_all_checkbox2);
                checkBox.setChecked(false);
            }
        });
    }

    private void movement(ArrayList<Players> add, ArrayList<Players> remove, ArrayList<Players> selected){
        if (selected.isEmpty())
            Toast.makeText(this, getString(R.string.nothing_selected), Toast.LENGTH_LONG).show();
        else {
            add.addAll(selected);
            remove.removeAll(selected);
            selected.clear();

            //ArrayList<String> fullName = playersToString(availablePlayers);
            ArrayAdapter<Players> adapter = new ArrayAdapter<>(this, R.layout.list_players, R.id.simple_checked_text_view, availablePlayers);
            allPlayersListView.setAdapter(adapter);

            //fullName = playersToString(chosenPlayers);
            adapter = new ArrayAdapter<>(this, R.layout.list_players, R.id.simple_checked_text_view, chosenPlayers);
            chosenPlayerListView.setAdapter(adapter);
        }
    }

    private void initListView(ListView listView, ArrayList<Players> playersList, final ArrayList<Players> selected){

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        ArrayAdapter<Players> adapter = new ArrayAdapter<>(this, R.layout.list_players, R.id.simple_checked_text_view,playersList );
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Players selectedPlayer = (Players) parent.getItemAtPosition(position);

                if (selected.contains(selectedPlayer))
                    selected.remove(selectedPlayer); //uncheck player
                else
                    selected.add(selectedPlayer);
            }
        });

    }



    private void addNewPlayer(){
        Button addPlayerButton = findViewById(R.id.add_player_button);

        addPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                dialog.setContentView(R.layout.popup_add_new_player);
                closeButton = dialog.findViewById(R.id.close_button);
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                final AddPlayer addPlayer = new AddPlayer(CreateTournament.this, dialog, getSupportFragmentManager());
                addPlayer.init();
                if (addPlayer.getPlayers() != null) {
                    availablePlayers.add(addPlayer.getPlayers());
                    Toast.makeText(CreateTournament.this, getString(R.string.added_player), Toast.LENGTH_LONG).show();

                }
                dialog.show();*/
                Intent i = new Intent(getApplicationContext(), AddNewPlayer.class);
                i.putExtra(getString(R.string.available_players), availablePlayers);
                startActivity(i);
            }

        });

    }

    private void configureTournament(){
        Button configureTournamentButton = findViewById(R.id.config_tournament_button);
        configureTournamentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                if (chosenPlayers.size() % 2 == 0) {
                    Intent i = new Intent(getApplicationContext(), ConfigureTournament.class);
                    i.putExtra(getString(R.string.players), chosenPlayers);
                    startActivity(i);
                }
                else {
                    chooseDialogBox = false;
                    GeneralDialogFragment dialog = GeneralDialogFragment.
                            newInstance(getString(R.string.title_warning), getString(R.string.odd_number_players), getString(R.string.positive_button_warning));
                    dialog.show(getSupportFragmentManager(), getString(R.string.title_warning));
                }*/


                Intent i = new Intent(getApplicationContext(), ConfigureTournament.class);
                i.putExtra(getString(R.string.players), chosenPlayers);//TODO for testing
                startActivity(i);



            }
        });
    }


    private void aboutPlayer(){
        Button aboutPlayerButton = findViewById(R.id.about_player);
        aboutPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setContentView(R.layout.popup_about_player);
                TextView name = dialog.findViewById(R.id.about_set_name_text_view);
                TextView surname = dialog.findViewById(R.id.about_set_surname_text_view);
                TextView date = dialog.findViewById(R.id.about_set_date_of_birth_text_view);
                TextView polishRank = dialog.findViewById(R.id.about_set_polish_ranking_text_view);
                TextView internationalRank = dialog.findViewById(R.id.about_set_international_ranking_text_view);

                if (selectedAvailablePlayers.size() == 1) {
                    Players p = selectedAvailablePlayers.get(0);
                    name.setText(p.getName());
                    surname.setText(p.getSurname());
                    date.setText(p.getDateOfBirth().toString());
                    polishRank.setText(String.valueOf(p.getPolishRanking()));
                    internationalRank.setText(String.valueOf(p.getInternationalRanking()));
                } else if (selectedAvailablePlayers.size() > 1) {
                    //todo selected more than one player
                } else  {
                    //todo not selected player
                }
                dialog.show();
            }
        });
    }

    private void removePlayer(){
        Button removePlayerButton = findViewById(R.id.remove_player);
        removePlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedAvailablePlayers.size() == 1) {
                    chooseDialogBox = true;
                    Players player = selectedAvailablePlayers.get(0);
                    GeneralDialogFragment dialog = GeneralDialogFragment.newInstance(
                            getString(R.string.title_warning),
                            getString(R.string.remove_player_warning,
                                    player.getName(),
                                    player.getSurname(),
                                    player.getDateOfBirth().toString(),
                                    player.getPolishRanking(),
                                    player.getInternationalRanking()),
                            getString(R.string.positive_button_warning));

                    dialog.show(getSupportFragmentManager(), getString(R.string.title_warning));




            } else if (selectedAvailablePlayers.size() > 1) {
                //todo selected more than one player
            } else  {
                //todo not selected player
            }

            }
        });
    }

    public void selectedAll(int idCheckBox, final ListView listView, final ArrayList<Players> list ){
        final CheckBox selectAllCheckBox = findViewById(idCheckBox);

        selectAllCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int itemCount = listView.getCount();
                list.clear();

                if (selectAllCheckBox.isChecked()) { //select all
                    for(int i=0 ; i < itemCount ; i++){
                        listView.setItemChecked(i, true);
                        list.add((Players) listView.getItemAtPosition(i));
                    }
                } else {
                    for(int i=0 ; i < itemCount ; i++){ //deselect all
                        listView.setItemChecked(i, false);
                    }
                }
            }
        });

    }

    @Override
    public void onOkClicked(GeneralDialogFragment dialog) {
        if (chooseDialogBox){
            final Players player = selectedAvailablePlayers.get(0);
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    database.playersDao().removePlayer(player);
                }
            });
            selectedAvailablePlayers.remove(player);
            availablePlayers.remove(player);
            ArrayAdapter<Players> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_players, R.id.simple_checked_text_view, availablePlayers);
            allPlayersListView.setAdapter(adapter);
            Toast.makeText(this, getString(R.string.removed_player), Toast.LENGTH_LONG).show();
        }
        else {
            Intent i = new Intent(getApplicationContext(), ConfigureTournament.class);
            i.putExtra(getString(R.string.players), chosenPlayers);
            startActivity(i);
        }
    }

    @Override
    public void onCancelClicked(GeneralDialogFragment dialog) {
        //todo dialog.getTag -> check
        if (chooseDialogBox) {
            allPlayersListView.setItemChecked(allPlayersListView.getSelectedItemPosition(), false); //todo not reseting
        }
    }
}
