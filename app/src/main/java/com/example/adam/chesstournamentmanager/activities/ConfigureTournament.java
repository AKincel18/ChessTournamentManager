package com.example.adam.chesstournamentmanager.activities;

import android.content.Intent;
import android.content.res.Resources;
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
import com.example.adam.chesstournamentmanager.swissalgorithm.SwissAlgorithm;
import com.example.adam.chesstournamentmanager.model.Player;
import com.example.adam.chesstournamentmanager.staticdata.adapters.ListAdapter;
import com.example.adam.chesstournamentmanager.staticdata.dialogbox.GeneralDialogFragment;
import com.example.adam.chesstournamentmanager.staticdata.dialogbox.OnDialogFragmentClickListener;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ConfigureTournament extends AppCompatActivity implements OnDialogFragmentClickListener {

    private ArrayList<Player> players;

    private ListView listView;

    private Switch switch1;

    private boolean placeOrder = true; // true - buchholz, false - median buchholz

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_tournament);

        TextView countOfPlayersTextView = findViewById(R.id.count_of_players_text_view);
        Intent i = getIntent();
        players = (ArrayList<Player>) i.getSerializableExtra(getString(R.string.players));
        countOfPlayersTextView.setText(String.valueOf(players.size()));
        listView = findViewById(R.id.players_list_view);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        switch1 = findViewById(R.id.count_of_number_switch);

        initSpinnerPlaceOrder();
        comparatorByStandardOrder();

        int optimalCountOfRounds =(int)Math.ceil(Math.log(players.size()) / Math.log(2)); // ceil(log2(player's number)),  log2 = (Math.log(x) / Math.log(2));
        choiceRoundsSwitchImplementation(optimalCountOfRounds);
        startTournament(optimalCountOfRounds);
        //losuj();

    }



    private void initSpinnerPlaceOrder(){
        Spinner spinner = findViewById(R.id.place_order_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.place_order, R.layout.color_spinner_config_layout);
        adapter.setDropDownViewResource(R.layout.spinner_config_tournament);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    placeOrder = parent.getSelectedItemPosition() == 0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void initListViewHeader() {
        ViewGroup headerView = (ViewGroup) getLayoutInflater().inflate(R.layout.start_list_header, listView, false);
        listView.addHeaderView(headerView);

        ListAdapter adapter = new ListAdapter(this, R.layout.row_layout, R.id.name_player_row_layout, getPlayerList());
        listView.setAdapter(adapter);
    }

    private List<String> getPlayerList(){
        List<String> list = new ArrayList<>();
        int counter = 1;
        for (Player player : players) {
            String internationalRanking = (player.getInternationalRanking() == -1 ? getString(R.string.no_rank) : String.valueOf(player.getInternationalRanking()));
            String polishRanking = (player.getPolishRanking() == -1 ? getString(R.string.no_rank) : String.valueOf(player.getPolishRanking()));
            String tmp = String.valueOf(counter) + ".," + player.toString() + "," + internationalRanking + "," + polishRanking;
            list.add(tmp);
            counter++;
        }
        return list;
    }

    private void comparatorByStandardOrder(){
        Collections.sort(players, new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                int c = Float.compare(o2.getInternationalRanking(), o1.getInternationalRanking());
                if (c == 0) {
                    c = Float.compare(o2.getPolishRanking(), o1.getPolishRanking());
                    if (c == 0) {
                        c = o1.toString().compareTo(o2.toString());
                    }
                }
                return c;
            }
        });

        initListViewHeader();
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
                        if (maxNumberCount() >= roundNumber && roundNumber != 0 ) {
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
                    i.putExtra(getString(R.string.place_order), placeOrder);
                    SwissAlgorithm.resetTournament();
                    startActivity(i);
                }


            }
        });

    }

    private int maxNumberCount(){
        if (players.size() % 2 == 0)
            return players.size() - 1;
        else
            return players.size();
    }

    private void choiceRoundsSwitchImplementation(int optimalCountOfRounds){
        final LinearLayout linearLayout = findViewById(R.id.layout_set_round);
        final TextView textView = new TextView(this);
        editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setGravity(Gravity.START | Gravity.CENTER);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        editText.setTextColor(getColor(R.color.colorPrimaryDark));


        textView.setText(getString(R.string.auto_count_of_rounds, optimalCountOfRounds));

        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);

        int startMargins = (int)(10 * Resources.getSystem().getDisplayMetrics().density + 0.5f);
        params.setMargins(startMargins, 0 , 0,0);

        textView.setGravity(Gravity.START | Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        textView.setTextColor(getColor(R.color.colorPrimaryDark));
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

    @Override
    public void onOkClicked(GeneralDialogFragment dialog) {

    }

    @Override
    public void onCancelClicked(GeneralDialogFragment dialog) {

    }
}
