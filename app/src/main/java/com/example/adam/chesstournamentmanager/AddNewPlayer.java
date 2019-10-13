package com.example.adam.chesstournamentmanager;

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
import java.util.Calendar;
import java.util.Locale;

import staticdata.Constans;
import staticdata.DialogBox;

public class AddNewPlayer extends FragmentActivity {

    private TextView pickDateTextView;
    private DatePickerDialog.OnDateSetListener dateSetListener;


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

        pickDateTextView = findViewById(R.id.pickDateTextView);
        Locale locale = new Locale("pl");
        Locale.setDefault(locale);

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


    }

    public void confirmNewPlayer(View view){
        EditText name =  findViewById(R.id.nameEditText);
        EditText surname = findViewById(R.id.surnameEditText);

        Intent i = new Intent(this, CreateTournament.class);
        i.putExtra("data", name.getText() + " " + surname.getText());
        startActivity(i);


    }

    public void close(View view){
        DialogBox dialogBox = new DialogBox();
        dialogBox.show(getSupportFragmentManager(), Constans.TTILE_DB);
    }


}
