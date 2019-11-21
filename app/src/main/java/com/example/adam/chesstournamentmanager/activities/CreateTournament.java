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
import java.util.List;
import java.util.concurrent.Executors;

import com.example.adam.chesstournamentmanager.R;
import com.example.adam.chesstournamentmanager.database.Database;
import com.example.adam.chesstournamentmanager.model.Players;
import com.example.adam.chesstournamentmanager.staticdata.dialogbox.GeneralDialogFragment;
import com.example.adam.chesstournamentmanager.staticdata.dialogbox.OnDialogFragmentClickListener;

import static com.example.adam.chesstournamentmanager.staticdata.FormatDateToString.getFormatDate;

public class CreateTournament extends AppCompatActivity implements OnDialogFragmentClickListener {

    private ArrayList<Players> selectedAvailablePlayers = new ArrayList<>();
    private ArrayList<Players> selectedChosenPlayers = new ArrayList<>();

    private ListView chosenPlayerListView;
    private ListView allPlayersListView;

    static private ArrayList<Players> availablePlayers = new ArrayList<>();
    static private ArrayList<Players> chosenPlayers = new ArrayList<>();

    private Database database;

    private Dialog dialog;



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
            initListView(allPlayersListView, availablePlayers, selectedAvailablePlayers);
        }
        else {
            fetchPlayers();
        }

        String message;
        if ( (message = i.getStringExtra(getString(R.string.toast_message))) != null) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }

        //buttons
        moveToChosenPlayers();
        moveToAvailablePlayers();
        addNewPlayer();
        configureTournament();
        aboutPlayer();
        removePlayer();
        editPlayer();

        selectedAll(R.id.select_all_checkbox, allPlayersListView, selectedAvailablePlayers);
        selectedAll(R.id.select_all_checkbox2, chosenPlayerListView, selectedChosenPlayers);

        initListView(chosenPlayerListView, chosenPlayers, selectedChosenPlayers);


        dialog = new Dialog(this);

   }





    private void fetchPlayers(){

        chosenPlayers.clear();
        availablePlayers.clear();
        Executors.newSingleThreadExecutor().execute(    new Runnable() {
            @Override
            public void run() {
                availablePlayers.addAll(database.playersDao().getAllPlayers());
                initListView(allPlayersListView, availablePlayers, selectedAvailablePlayers);
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
                if (chosenPlayers.size() < 2) {
                    GeneralDialogFragment dialog = GeneralDialogFragment.
                            newInstance(getString(R.string.too_many_players_title),
                                    getString(R.string.too_many_players_message),
                                    getString(R.string.exit_button));
                    dialog.show(getSupportFragmentManager(), getString(R.string.too_many_players_title));
                }
                else {
                    if (chosenPlayers.size() % 2 == 0) {
                        Intent i = new Intent(getApplicationContext(), ConfigureTournament.class);
                        i.putExtra(getString(R.string.players), chosenPlayers);
                        startActivity(i);
                    } else {
                        GeneralDialogFragment dialog = GeneralDialogFragment.
                                newInstance(getString(R.string.title_warning), getString(R.string.odd_number_players), getString(R.string.positive_button_warning));
                        dialog.show(getSupportFragmentManager(), getString(R.string.title_warning));
                    }
                }

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
                TextView date = dialog.findViewById(R.id.about_set_date_of_birth_text_view);
                TextView polishRank = dialog.findViewById(R.id.about_set_polish_ranking_text_view);
                TextView internationalRank = dialog.findViewById(R.id.about_set_international_ranking_text_view);

                if (selectedAvailablePlayers.size() == 1) {
                    Players p = selectedAvailablePlayers.get(0);
                    name.setText(p.toString());
                    date.setText(getFormatDate(p.getDateOfBirth()));
                    polishRank.setText(p.getPolishRanking() != -1 ? String.valueOf(p.getPolishRanking()) : getString(R.string.no_rank));
                    internationalRank.setText(p.getInternationalRanking() != -1 ? String.valueOf(p.getInternationalRanking()) : getString(R.string.no_rank));
                    dialog.show();
                } else if (selectedAvailablePlayers.size() > 1) {
                    moreThanOnePlayerSelectedDialogBox();
                } else  {
                    noOnePlayerSelectedDialogBox();
                }
            }
        });
    }

    private GeneralDialogFragment getDialog(Players player){
        String polishRanking = player.getPolishRanking() != -1 ? String.valueOf(player.getPolishRanking()) : getString(R.string.no_rank);
        String internationalRanking = player.getInternationalRanking() != -1 ? String.valueOf(player.getInternationalRanking()) : getString(R.string.no_rank);

        return GeneralDialogFragment.newInstance(
                getString(R.string.remove_player_title_DB),
                getString(R.string.remove_player_warning,
                player.getName(),
                player.getSurname(),
                getFormatDate(player.getDateOfBirth()),
                polishRanking,
                internationalRanking), getString(R.string.positive_button_warning));
    }

    private void removePlayer(){
        Button removePlayerButton = findViewById(R.id.remove_player);
        removePlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedAvailablePlayers.size() == 1) {
                    Players player = selectedAvailablePlayers.get(0);
                    GeneralDialogFragment dialog = getDialog(player);
                    dialog.show(getSupportFragmentManager(), getString(R.string.confirmation_removing_player));

            } else if (selectedAvailablePlayers.size() > 1) {
                    GeneralDialogFragment dialog = GeneralDialogFragment.newInstance(
                            getString(R.string.title_warning),
                            getString(R.string.selected_player_remove),
                            getString(R.string.positive_button_warning));
                    dialog.show(getSupportFragmentManager(), getString(R.string.confirmation_removing_player));
                } else  {
                    noOnePlayerSelectedDialogBox();
            }

            }
        });
    }

    private void editPlayer(){
        Button editPlayerButton = findViewById(R.id.edit_player);
        editPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedAvailablePlayers.size() == 1) {
                    availablePlayers.remove(selectedAvailablePlayers.get(0));
                    Intent i = new Intent(getApplicationContext(), AddNewPlayer.class);
                    i.putExtra(getString(R.string.available_players), availablePlayers);
                    i.putExtra(getString(R.string.player), selectedAvailablePlayers.get(0));
                    startActivity(i);
                } else if (selectedAvailablePlayers.size() > 1) {
                    moreThanOnePlayerSelectedDialogBox();
                } else  {
                    noOnePlayerSelectedDialogBox();
                }
            }
        });
    }

    private void moreThanOnePlayerSelectedDialogBox() {
        GeneralDialogFragment dialog = GeneralDialogFragment.newInstance(
                getString(R.string.title_error),
                getString(R.string.too_many_player_selected),
                getString(R.string.positive_button_warning));
        dialog.show(getSupportFragmentManager(), getString(R.string.about_player_db));
    }

    private void noOnePlayerSelectedDialogBox() {

        GeneralDialogFragment dialog = GeneralDialogFragment.newInstance(
                getString(R.string.title_error),
                getString(R.string.no_one_selected),
                getString(R.string.positive_button_warning));
        dialog.show(getSupportFragmentManager(), getString(R.string.about_player_db));
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
        final List<Players> playersList = new ArrayList<>(selectedAvailablePlayers);

        if (dialog.getTag().equals(getString(R.string.confirmation_removing_player))){
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    database.playersDao().removePlayer(playersList);
                }
            });

            availablePlayers.removeAll(selectedAvailablePlayers);
            selectedAvailablePlayers.clear();

            ArrayAdapter<Players> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_players, R.id.simple_checked_text_view, availablePlayers);
            allPlayersListView.setAdapter(adapter);
            if (playersList.size() == 1)
                Toast.makeText(this, getString(R.string.removed_player), Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, getString(R.string.removed_players), Toast.LENGTH_LONG).show();

        }
        else if (dialog.getTag().equals(getString(R.string.title_warning))){
            Intent i = new Intent(getApplicationContext(), ConfigureTournament.class);
            i.putExtra(getString(R.string.players), chosenPlayers);
            startActivity(i);
        }
    }

    @Override
    public void onCancelClicked(GeneralDialogFragment dialog) {
        if (dialog.getTag().equals(getString(R.string.confirmation_removing_player))) {
            selectedAvailablePlayers.clear();
            initListView(allPlayersListView, availablePlayers, selectedAvailablePlayers);
            CheckBox checkBox = findViewById(R.id.select_all_checkbox);
            checkBox.setChecked(false);
        }
    }
}
