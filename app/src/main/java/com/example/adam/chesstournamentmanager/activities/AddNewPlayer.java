package com.example.adam.chesstournamentmanager.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;

import com.example.adam.chesstournamentmanager.R;
import com.example.adam.chesstournamentmanager.database.Database;
import com.example.adam.chesstournamentmanager.model.Players;
import com.example.adam.chesstournamentmanager.staticdata.Constans;
import com.example.adam.chesstournamentmanager.staticdata.DialogBox;

public class AddNewPlayer extends FragmentActivity {

    private TextView pickDateTextView;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    private ArrayList<String> allPlayers;

    private Database database;


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
                String date = dayOfMonth + "-" + month + "-" + year;
                pickDateTextView.setText(date);
            }
        };

        Intent i = getIntent();
        allPlayers = i.getStringArrayListExtra("availablePlayers");




    }

    public void confirmNewPlayer(View view){
        final EditText name =  findViewById(R.id.nameEditText);
        final EditText surname = findViewById(R.id.surnameEditText);
        final TextView date = findViewById(R.id.pickDateTextView);

        Intent i = new Intent(this, CreateTournament.class);
        if (allPlayers == null)
            allPlayers = new ArrayList<>();


        allPlayers.add(name.getText() + " " + surname.getText());

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                Date formatDate = new Date();
                try {
                    formatDate = format.parse(date.getText().toString());
                }
                catch (ParseException exc) {
                    System.out.println("WRONG DATE");
                }

                database.playersDao().insertPlayer(new Players(name.getText().toString(), surname.getText().toString(), formatDate));
            }
        });

        i.putStringArrayListExtra("availablePlayers", allPlayers);
        startActivity(i);


    }

    public void close(View view){
        DialogBox dialogBox = new DialogBox();
        dialogBox.show(getSupportFragmentManager(), Constans.TTILE_DB);
    }


}
