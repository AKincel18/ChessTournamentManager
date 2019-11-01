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
import com.example.adam.chesstournamentmanager.model.Players;
import com.example.adam.chesstournamentmanager.staticdata.Constans;
import com.example.adam.chesstournamentmanager.staticdata.dialogbox.GeneralDialogFragment;

public class AddNewPlayer extends FragmentActivity implements GeneralDialogFragment.OnDialogFragmentClickListener {

    private TextView pickDateTextView;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    private ArrayList<Players> allPlayers;

    private Database database;

    private Date formatDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_add_new_player);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (width*.6), (int) (height*.6));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = 0;

        getWindow().setAttributes(params);

        database = Database.getInstance(this);

        pickDateTextView = findViewById(R.id.pickDateTextView);
/*        Locale locale = new Locale("pl");
        Locale.setDefault(locale);*/

        pickDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddNewPlayer.this,
                        android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth,
                        dateSetListener,
                        year, month, day);

                dialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "-" + (month + 1) + "-" + year; //begin value in month = 0
                pickDateTextView.setText(date);
            }
        };

        Intent i = getIntent();
        allPlayers = (ArrayList<Players>) i.getSerializableExtra("availablePlayers");

        //buttons
        confirmNewPlayer();
        close();

    }

    public void confirmNewPlayer(){

        Button confirmPlayerButton = findViewById(R.id.confirmNewPlayerButton);
        confirmPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText name =  findViewById(R.id.nameEditText);
                final EditText surname = findViewById(R.id.surnameEditText);
                final TextView date = findViewById(R.id.pickDateTextView);
                final EditText polishRanking = findViewById(R.id.polishRankingNumber);
                final EditText internationalRanking = findViewById(R.id.internationalRankingNumber);


                DateFormat format = new SimpleDateFormat("dd-MM-yyyy", new Locale("pl"));

                formatDate = new Date();
/*                try {
                    formatDate = format.parse(date.getText().toString());
                }
                catch (ParseException exc) {
                    GeneralDialogFragment generalDialogFragment =
                            GeneralDialogFragment.newInstance(Constans.TITLE_ERROR_DIALOGBOX, Constans.MESSAGE_ERROR_DIALOGBOX, Constans.EXIT_BUTTON_ERROR_DIALOGBOX);
                    generalDialogFragment.show(getSupportFragmentManager(), Constans.TITLE_ERROR_DIALOGBOX);
                    return;


                }*/

                final Players players = new Players(
                        name.getText().toString(),
                        surname.getText().toString(),
                        formatDate
                );

                try{
                    players.setPolishRanking(Integer.valueOf(polishRanking.getText().toString()));
                }
                catch (NumberFormatException e){
                    players.setPolishRanking(Integer.MAX_VALUE);
                    }
                try {
                    players.setInternationalRanking(Integer.valueOf(internationalRanking.getText().toString()));
                }
                catch (NumberFormatException e){
                    players.setInternationalRanking(Integer.MAX_VALUE);
                }

                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        database.playersDao().insertPlayer(players);

                    }
                });

                Intent i = new Intent(getApplicationContext(), CreateTournament.class);
                if (allPlayers == null)
                    allPlayers = new ArrayList<>();

                allPlayers.add(players);
                i.putExtra("availablePlayers", allPlayers);
                startActivity(i);
            }
        });

    }

    private void close(){
        Button closeButton = findViewById(R.id.closeButton);
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
                GeneralDialogFragment.newInstance(Constans.TITLE_WARNING, Constans.INFORMATION_WARNING, Constans.POSITIVE_BUTTON_WARNING);
        generalDialogFragment.show(getSupportFragmentManager(), Constans.TITLE_WARNING);
    }

    @Override
    public void onOkClicked(GeneralDialogFragment dialog) {
        if (dialog.getArguments().getString("title").equals(Constans.TITLE_WARNING)) {
            Intent i = new Intent(this, CreateTournament.class);
            startActivity(i);
        }
    }

    @Override
    public void onCancelClicked(GeneralDialogFragment dialog) {
    }
}
