package com.example.adam.chesstournamentmanager.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.adam.chesstournamentmanager.R;
import com.example.adam.chesstournamentmanager.database.Database;
import com.example.adam.chesstournamentmanager.model.Players;
import com.example.adam.chesstournamentmanager.staticdata.dialogbox.GeneralDialogFragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;

public class AddPlayer implements GeneralDialogFragment.OnDialogFragmentClickListener{

    private TextView pickDateTextView;

    private DatePickerDialog.OnDateSetListener dateSetListener;

    private Database database;

    private Date formatDate;

    private Context context;

    private Dialog dialog;

    private Players players;


    //private FragmentActivity fragmentActivity;

    private FragmentManager fragmentManager;

    public AddPlayer(Context context, Dialog dialog, FragmentManager fragmentManager) {
        this.context = context;
        this.dialog = dialog;
        this.fragmentManager = fragmentManager;
    }

    public void init(){
        database = Database.getInstance(context);

        pickDateTextView = dialog.findViewById(R.id.pick_date_text_view);
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
                        context,
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


        //buttons
        close();
        confirmNewPlayer();

    }


    private void confirmNewPlayer(){

        Button confirmPlayerButton = dialog.findViewById(R.id.confirm_new_player_button);
        confirmPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText name =  dialog.findViewById(R.id.name_edit_text);
                final EditText surname = dialog.findViewById(R.id.surname_edit_text);
                final TextView date = dialog.findViewById(R.id.pick_date_text_view);
                final EditText polishRanking = dialog.findViewById(R.id.polish_ranking_number);
                final EditText internationalRanking = dialog.findViewById(R.id.international_ranking_number);


                DateFormat format = new SimpleDateFormat("dd-MM-yyyy", new Locale("pl"));

                formatDate = new Date();
                try {
                    formatDate = format.parse(date.getText().toString());
                }
                catch (ParseException exc) {
                    GeneralDialogFragment generalDialogFragment =
                            GeneralDialogFragment.newInstance(Resources.getSystem().getString(R.string.title_error),
                                    Resources.getSystem().getString(R.string.message_error),
                                    Resources.getSystem().getString(R.string.exit_button));
                    generalDialogFragment.show(fragmentManager, Resources.getSystem().getString(R.string.title_error));
                    return;


                }

                players = new Players(
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

            }
        });

    }

    private void close(){
        Button closeButton = dialog.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogBox();
            }
        });
    }

    private void showDialogBox() {
        GeneralDialogFragment generalDialogFragment =
                GeneralDialogFragment.newInstance(Resources.getSystem().getString(R.string.title_warning),
                        Resources.getSystem().getString(R.string.information_warning),
                        Resources.getSystem().getString(R.string.positive_button_warning));

        generalDialogFragment.show(fragmentManager, Resources.getSystem().getString(R.string.title_warning));
    }

    @Override
    public void onOkClicked(GeneralDialogFragment dialog) {

    }

    @Override
    public void onCancelClicked(GeneralDialogFragment dialog) {
        dialog.dismiss();
    }

    public Players getPlayers() {
        return players;
    }

    public void setPlayers(Players players) {
        this.players = players;
    }
}
