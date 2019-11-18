package com.example.adam.chesstournamentmanager.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.adam.chesstournamentmanager.R;
import com.example.adam.chesstournamentmanager.SwissAlgorithm;
import com.example.adam.chesstournamentmanager.model.Players;
import com.example.adam.chesstournamentmanager.staticdata.Constans;
import com.example.adam.chesstournamentmanager.staticdata.dialogbox.GeneralDialogFragment;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Formatter;

public class ConfigureTournament extends AppCompatActivity implements GeneralDialogFragment.OnDialogFragmentClickListener{

    private ArrayList<Players> players;
    private ListView listView;
    private Switch switch1;
    private Switch switch2;
    private String order;
    private int placeOrder;
    private EditText editText;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_tournament);

        TextView countOfPlayersTextView = findViewById(R.id.count_of_players_text_view);
        Intent i = getIntent();
        players = (ArrayList<Players>) i.getSerializableExtra(getString(R.string.players));
        countOfPlayersTextView.setText(String.valueOf(players.size()));
        listView = findViewById(R.id.players_list_view);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        //String []pairingType = getResources().getStringArray(R.array.pairing);
        //Spinner spinner = findViewById(R.id.pairing_type_spinner);


        switch1 = findViewById(R.id.count_of_number_switch);
        switch2 = findViewById(R.id.order_place_switch);

        initSpinnerPlaceOrder();
        initSpinnerPlayerOrder();
        comparatorByStandardOrder();

        int optimalCountOfRounds =(int)Math.ceil(Math.log(players.size()) / Math.log(2)); // ceil(log2(player's number)),  log2 = (Math.log(x) / Math.log(2));
        choiceRoundsSwitchImplementation(optimalCountOfRounds);
        orderPlayerSwitchImplementation();
        startTournament(optimalCountOfRounds);
        //losuj();


    }


    private void initSpinnerPlaceOrder(){
        Spinner spinner = findViewById(R.id.place_order_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.place_order, R.layout.spinner_item_config_tournament);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                placeOrder = parent.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void initSpinnerPlayerOrder(){
        spinner = new Spinner(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.pairing, R.layout.spinner_item_config_tournament);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //final String []pairingType = getResources().getStringArray(R.array.pairing);

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
            String tmp = p.toString();
            if (p.getPolishRanking() != -1){
                fmt.format("%10f", p.getPolishRanking());
                s.add(tmp + fmt.toString());
            }
            else{
                s.add(tmp);
            }
        initListViewByRanking(s);

        }

    }

    private void internationalRanking(){

        ArrayList<String> s = new ArrayList<>();
        for (Players p : players) {
            Formatter fmt = new Formatter();
            if (p.getInternationalRanking() != -1){
                fmt.format("%1s %20s %15f", p.getSurname(), p.getName(), p.getInternationalRanking());
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
                int c = Float.compare(o2.getPolishRanking(), o1.getPolishRanking());
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
                int c = Float.compare(o2.getInternationalRanking(), o1.getInternationalRanking());
                if (c == 0)
                    c = o1.getSurname().compareTo(o2.getSurname());
                if (c == 0)
                    return o1.getName().compareTo(o2.getName());
                return c;
            }
        });
    }

    private void comparatorByStandardOrder(){
        Collections.sort(players, new Comparator<Players>() {
            @Override
            public int compare(Players o1, Players o2) {
                int c = Float.compare(o2.getInternationalRanking(), o1.getInternationalRanking());
                if (c == 0) {
                    c = Float.compare(o2.getPolishRanking(), o1.getPolishRanking());
                    if (c == 0) {
                        c = o1.getSurname().compareTo(o2.getSurname());
                        if (c == 0) {
                            c = o1.getName().compareTo(o2.getName());
                        }
                    }
                }
                return c;
            }
        });


        initListView();
    }


/*    private void losuj(){
        Button button = findViewById(R.id.button);
        final EditText roundsNumber = findViewById(R.id.rounds_number_edit_text);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    SwissAlgorithm swissAlgorithm = SwissAlgorithm.initSwissAlgorithm(Integer.valueOf(roundsNumber.getText().toString()), Constans.ALPHABETICAL_ORDER, 0);
                    swissAlgorithm.initTournamentPlayers(players);
                    swissAlgorithm.drawFirstRound();

            }
        });
    }*/

    private void startTournament(final int optimalCountOfRounds) {
        Button startTournament = findViewById(R.id.start_tournament_button);
        startTournament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Tournament.class);
                boolean startActivity = false;
                if (switch1.isChecked()){

                    try {
                        int roundNumber = Integer.valueOf(editText.getText().toString());
                        if (isGoodNumberOfRounds(roundNumber)) {
                            startActivity = true;
                            i.putExtra(getString(R.string.rounds_number), roundNumber);

                        } else {
                            GeneralDialogFragment dialog = GeneralDialogFragment.newInstance(
                                    getString(R.string.wrong_rounds_title),
                                    getString(R.string.wrong_rounds_message, players.size(), maxNumberCount(), roundNumber ),
                                    getString(R.string.exit_button));
                                    dialog.show(getSupportFragmentManager(), getString(R.string.wrong_rounds_title));


                            }


                    } catch (NumberFormatException e) {
                            GeneralDialogFragment dialog = GeneralDialogFragment.newInstance(
                                    getString(R.string.title_error),
                                    getString(R.string.empty_rounds_number),
                                    getString(R.string.exit_button));
                                    dialog.show(getSupportFragmentManager(),getString(R.string.title_error));
                    }


                }
                else {
                    startActivity = true;
                    i.putExtra(getString(R.string.rounds_number), Integer.valueOf(optimalCountOfRounds));
                }

                if (startActivity){
                    i.putExtra(getString(R.string.players), players);
                    i.putExtra(getString(R.string.order), order);
                    i.putExtra(getString(R.string.place_order), placeOrder);
                    SwissAlgorithm.resetTournament();
                    startActivity(i);
                }


            }
        });

    }

    private boolean isGoodNumberOfRounds(int roundsNumber){
        if (players.size() % 2 == 0)
            return players.size() > roundsNumber && roundsNumber != 0;
        else
            return players.size() >= roundsNumber && roundsNumber != 0;
    }

    private int maxNumberCount(){
        if (players.size() % 2 == 0)
            return players.size() - 1;
        else
            return players.size();
    }

    private void choiceRoundsSwitchImplementation(int optimalCountOfRounds){
/*        final TextView textView = findViewById(R.id.auto_count_of_rounds);
        final EditText editText = findViewById(R.id.rounds_number_edit_text);*/
        final LinearLayout linearLayout = findViewById(R.id.layout_set_round);
        final TextView textView = new TextView(this);
        editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setGravity(Gravity.START | Gravity.CENTER);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        editText.setTextColor(Color.BLACK);


        textView.setText(getString(R.string.auto_count_of_rounds, optimalCountOfRounds));

        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);

        int startMargins = (int)(10 * Resources.getSystem().getDisplayMetrics().density + 0.5f);
        params.setMargins(startMargins, 0 , 0,0);

        textView.setGravity(Gravity.START | Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        textView.setTextColor(Color.GRAY);
        textView.setLayoutParams(params);
        linearLayout.addView(textView);


        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    editText.setLayoutParams(params);
                    linearLayout.removeView(textView);
                    linearLayout.addView(editText);
                }
                else{
                    textView.setLayoutParams(params);
                    linearLayout.removeView(editText);
                    linearLayout.addView(textView);

                }
            }
        });
    }

    private void orderPlayerSwitchImplementation(){

        final LinearLayout linearLayout = findViewById(R.id.layout_order_player);

        final TextView textView = new TextView(this);



        textView.setText(getString(R.string.auto_order_player));

        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);

        int startMargins = (int)(10 * Resources.getSystem().getDisplayMetrics().density + 0.5f);
        params.setMargins(startMargins, 0 , 0,0);

        textView.setGravity(Gravity.START | Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        textView.setTextColor(Color.GRAY);
        textView.setLayoutParams(params);
        linearLayout.addView(textView);


        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    spinner.setLayoutParams(params);
                    linearLayout.removeView(textView);
                    linearLayout.addView(spinner);
                }
                else{
                    textView.setLayoutParams(params);
                    linearLayout.removeView(spinner);
                    linearLayout.addView(textView);
                    comparatorByStandardOrder();
                }
            }
        });
    }

    @Override
    public void onOkClicked(GeneralDialogFragment dialog) {

    }

    @Override
    public void onCancelClicked(GeneralDialogFragment dialog) {

    }
}
