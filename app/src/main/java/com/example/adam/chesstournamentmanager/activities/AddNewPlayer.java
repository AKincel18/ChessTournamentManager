package com.example.adam.chesstournamentmanager.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;

import com.example.adam.chesstournamentmanager.R;
import com.example.adam.chesstournamentmanager.database.Database;
import com.example.adam.chesstournamentmanager.model.Player;
import com.example.adam.chesstournamentmanager.staticdata.dialogbox.GeneralDialogFragment;
import com.example.adam.chesstournamentmanager.staticdata.dialogbox.OnDialogFragmentClickListener;

import static com.example.adam.chesstournamentmanager.staticdata.FormatDateToString.parseDateFromDatabase;
import static com.example.adam.chesstournamentmanager.staticdata.FormatDateToString.parseDateFromDatePicker;

public class AddNewPlayer extends FragmentActivity implements OnDialogFragmentClickListener {

    private TextView pickDateTextView;

    private DatePickerDialog.OnDateSetListener dateSetListener;

    private ArrayList<Player> allPlayers;

    private Database database;

    private Date formatDate;

    private boolean addNewPlayer;

    private Player editPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_add_new_player);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (width*.6), (int) (height*.66));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = 0;

        getWindow().setAttributes(params);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        database = Database.getInstance(this);



        Intent i = getIntent();
        allPlayers = (ArrayList<Player>) i.getSerializableExtra(getString(R.string.available_players));

        editPlayer = (Player) i.getSerializableExtra(getString(R.string.player));
        addNewPlayer = editPlayer == null;


        if (!addNewPlayer) { //edit player popup
            setValues(editPlayer);
            TextView textView = findViewById(R.id.add_edit_text_view);
            textView.setText(getString(R.string.edit_player));
        }

        confirmNewPlayer();
        pickDateListener();
        close();

    }


    private void pickDateListener(){
        pickDateTextView = findViewById(R.id.pick_date_text_view);

        pickDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddNewPlayer.this,
                        R.style.datePicker,
                        dateSetListener,
                        year, month, day);

                dialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = parseDateFromDatePicker(year, month, dayOfMonth);
                pickDateTextView.setText(date);
            }
        };
    }


    private void setValues(Player player){
        EditText name = findViewById(R.id.name_edit_text);
        name.setText(player.getName());

        EditText surname = findViewById(R.id.surname_edit_text);
        surname.setText(player.getSurname());

        TextView date = findViewById(R.id.pick_date_text_view);
        date.setText(parseDateFromDatabase(player.getDateOfBirth()));

        EditText polishRank = findViewById(R.id.polish_ranking_number);
        if (player.getPolishRanking() != -1) {
            polishRank.setText(String.valueOf(player.getPolishRanking()));
        } else {
            polishRank.setHint(getString(R.string.no_rank));
        }

        EditText internationalRank = findViewById(R.id.international_ranking_number);
        if (player.getInternationalRanking() != -1) {
            internationalRank.setText(String.valueOf(player.getInternationalRanking()));
        } else {
            internationalRank.setHint(getString(R.string.no_rank));
        }
    }



    public void confirmNewPlayer(){

        Button confirmPlayerButton = findViewById(R.id.confirm_new_player_button);
        confirmPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText name =  findViewById(R.id.name_edit_text);
                final EditText surname = findViewById(R.id.surname_edit_text);
                final TextView date = findViewById(R.id.pick_date_text_view);
                final EditText polishRanking = findViewById(R.id.polish_ranking_number);
                final EditText internationalRanking = findViewById(R.id.international_ranking_number);


                DateFormat format = new SimpleDateFormat(getString(R.string.format_date), new Locale(getString(R.string.locale)));

                formatDate = new Date();
                try {
                    formatDate = format.parse(date.getText().toString());
                }
                catch (ParseException exc) {
                    GeneralDialogFragment generalDialogFragment =
                            GeneralDialogFragment.newInstance(getString(R.string.title_error), getString(R.string.message_error), getString(R.string.exit_button));
                    generalDialogFragment.show(getSupportFragmentManager(), getString(R.string.title_error));
                    return;


                }

                final Player player = new Player(
                        name.getText().toString(),
                        surname.getText().toString(),
                        formatDate
                );

                try{
                    player.setPolishRanking(Integer.valueOf(polishRanking.getText().toString()));
                }
                catch (NumberFormatException e){
                    player.setPolishRanking(-1);
                    }
                try {
                    player.setInternationalRanking(Integer.valueOf(internationalRanking.getText().toString()));
                }
                catch (NumberFormatException e){
                    player.setInternationalRanking(-1);
                }

                if (player.getName().equals(getString(R.string.empty_string)) || player.getSurname().equals(getString(R.string.empty_string))) {
                    GeneralDialogFragment dialog = GeneralDialogFragment.newInstance(
                            getString(R.string.title_error),
                            getString(R.string.required_fields),
                            getString(R.string.exit_button));
                    dialog.show(getSupportFragmentManager(), getString(R.string.title_error));

                } else {
                    if (addNewPlayer) {
                        saveNewPlayer(player);
                    } else {
                        updatePlayer(player);
                    }
                }


            }
        });

    }

    private void saveNewPlayer(final Player player){
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                database.playersDao().insertPlayer(player);
            }
        });

        Intent i = new Intent(getApplicationContext(), CreateTournament.class);
        if (allPlayers == null)
            allPlayers = new ArrayList<>();

        allPlayers.add(player);
        i.putExtra(getString(R.string.available_players), allPlayers);
        i.putExtra(getString(R.string.toast_message),getString(R.string.added_player));
        startActivity(i);
    }

    private void updatePlayer(final Player player){

        editPlayer.setName(player.getName());
        editPlayer.setSurname(player.getSurname());
        editPlayer.setPolishRanking(player.getPolishRanking());
        editPlayer.setInternationalRanking(player.getInternationalRanking());
        editPlayer.setDateOfBirth(player.getDateOfBirth());

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                database.playersDao().updatePlayer(editPlayer);
            }
        });

        Intent i = new Intent(getApplicationContext(), CreateTournament.class);
        if (allPlayers == null)
            allPlayers = new ArrayList<>();

        allPlayers.add(editPlayer);
        i.putExtra(getString(R.string.available_players), allPlayers);
        i.putExtra(getString(R.string.toast_message),getString(R.string.edited_player));
        startActivity(i);

    }

    private void close(){
        Button closeButton = findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogBox();
            }
        });
    }

    @Override
    public void onBackPressed(){
        showDialogBox();
    }

    private void showDialogBox(){
        GeneralDialogFragment generalDialogFragment =
                GeneralDialogFragment.newInstance(getString(R.string.title_warning), getString(R.string.information_warning), getString(R.string.positive_button_warning));
        generalDialogFragment.show(getSupportFragmentManager(), getString(R.string.title_warning));
    }

    @Override
    public void onOkClicked(GeneralDialogFragment dialog) {
        if (dialog.getArguments().getString(getString(R.string.title)).equals(getString(R.string.title_warning))) {
            Intent i = new Intent(this, CreateTournament.class);
            if (!addNewPlayer) {
                allPlayers.add(editPlayer);
                i.putExtra(getString(R.string.toast_message),getString(R.string.not_edit_player));
            }
            else {
                i.putExtra(getString(R.string.toast_message),getString(R.string.not_added_player));
            }
            i.putExtra(getString(R.string.available_players), allPlayers);
            startActivity(i);
        }
    }

    @Override
    public void onCancelClicked(GeneralDialogFragment dialog) {
    }
}
