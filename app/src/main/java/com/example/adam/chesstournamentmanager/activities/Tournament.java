package com.example.adam.chesstournamentmanager.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adam.chesstournamentmanager.matches.Match;
import com.example.adam.chesstournamentmanager.matches.MatchResult;
import com.example.adam.chesstournamentmanager.R;
import com.example.adam.chesstournamentmanager.swissalgorithm.SwissAlgorithm;
import com.example.adam.chesstournamentmanager.model.Player;
import com.example.adam.chesstournamentmanager.staticdata.adapters.SpinnerAdapter;
import com.example.adam.chesstournamentmanager.staticdata.dialogbox.GeneralDialogFragment;
import com.example.adam.chesstournamentmanager.staticdata.dialogbox.OnDialogFragmentClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tournament extends AppCompatActivity implements OnDialogFragmentClickListener {

    private TextView[] textViews;

    private  Spinner[] spinners;

    private SwissAlgorithm swissAlgorithm;

    private TextView titleTextView;

    private List<Match> matches;

    private Button nextRoundButton;

    private Menu myMenu;

    private ActionBarDrawerToggle actionBarDrawerToggle;

    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tournament);
        nextRoundButton = findViewById(R.id.next_round_button);
        titleTextView = findViewById(R.id.round_count_text_view);




        swissAlgorithm = SwissAlgorithm.getINSTANCE();

        if (swissAlgorithm == null) {

            Intent i = getIntent();
            int roundsNumber = i.getIntExtra(getString(R.string.rounds_number), 0);
            int placeOrder = i.getIntExtra(getString(R.string.place_order), 0);
            List<Player> players = (List<Player>) i.getSerializableExtra(getString(R.string.players));

            swissAlgorithm = SwissAlgorithm.initSwissAlgorithm(roundsNumber, placeOrder);
            swissAlgorithm.initTournamentPlayers(players);
            swissAlgorithm.drawFirstRound();

        }

        initNavigationMenu();
        buildRoundsView();
        nextRoundButton();
        buildMenu();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item) || actionBarDrawerToggle.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        GeneralDialogFragment dialog = GeneralDialogFragment.exitDialogBox();
        dialog.show(getSupportFragmentManager(),  getString(R.string.title_warning));
    }

    private void initNavigationMenu(){

        final DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);


        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        navigationView = findViewById(R.id.nav_view);
        myMenu = navigationView.getMenu();

        buildColorMenu(R.id.rounds_menu,R.style.titleMenuStyle );
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id != R.id.exit_menu) { //exit
                    if (id == R.id.results_menu) { //results
                        if (swissAlgorithm.getCurrentRound() != 1) {
                            Intent i = new Intent(getApplicationContext(), FinalResults.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.not_finished_first_round), Toast.LENGTH_LONG).show();
                        }
                    } else if (id >= 1) { //item rounds = (1, roundsNumber)
                        if (id < swissAlgorithm.getCurrentRound()) { //previous round
                            refreshView(id, false);
                        } else if (id > swissAlgorithm.getCurrentRound()) { //next round
                            Toast.makeText(getApplicationContext(), getString(R.string.previous_rounds_not_finished), Toast.LENGTH_LONG).show();
                        } else if (id == swissAlgorithm.getCurrentRound()) { //current round
                            drawerLayout.closeDrawer(GravityCompat.START);
                        }
                    }

                } else {
                    GeneralDialogFragment dialog = GeneralDialogFragment.exitDialogBox();
                    dialog.show(getSupportFragmentManager(), getString(R.string.title_warning));
                }

                return true;
            }
        });
    }

    private void buildColorMenu(int rId, int rStyle){
        MenuItem rounds = myMenu.findItem(rId);
        SpannableString s = new SpannableString(rounds.getTitle());
        s.setSpan(new TextAppearanceSpan(this, rStyle), 0, s.length(), 0);
        rounds.setTitle(s);
    }

    private void nextRoundButton(){
        nextRoundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<MatchResult> matchResults = getResult();
                if (matchResults != null) {
                    swissAlgorithm.setResult(matchResults);

                    if (swissAlgorithm.isFinishedTournament()) {
                        Intent i = new Intent(getApplicationContext(), FinalResults.class);
                        startActivity(i);
                    }
                    else {
                        swissAlgorithm.drawNextRound();
                        titleTextView.setText(getString(R.string.round_count_text_view, swissAlgorithm.getCurrentRound()));
                        ScrollView scrollView = findViewById(R.id.scroll_view);
                        scrollView.scrollTo(0,0);
                        refreshView(swissAlgorithm.getCurrentRound(), true);
                    }

                }
                else {
                    notAllResultsEnteredDialogBox();
                }

            }
        });
    }

    private void notAllResultsEnteredDialogBox(){
        GeneralDialogFragment generalDialogFragment =
                GeneralDialogFragment.newInstance(getString(R.string.title_error), getString(R.string.no_result_message_error), getString(R.string.exit_button));
        generalDialogFragment.show(getSupportFragmentManager(), getString(R.string.title_error));
    }

    private void buildMenu(){
        MenuItem menuItem = myMenu.findItem(R.id.rounds_menu);
        SubMenu subMenu = menuItem.getSubMenu();
        for (int i = 1; i <= swissAlgorithm.getRoundsNumber(); i++) {
            subMenu.add(Menu.NONE,i, Menu.NONE,getString(R.string.round_count_text_view, i));
            buildColorMenu(i, R.style.subMenuRoundsStyle);
        }
        navigationView.invalidate();

    }

    private List<MatchResult> getResult(){
        int matchCount = swissAlgorithm.isEven() ? matches.size() : matches.size() - 1;

        List<MatchResult> results = new ArrayList<>();
        for (int i=0; i < matchCount; i++){
            MatchResult m = getMatchResultFromSpinner(i);
            if (m != null)
                results.add(m);
            else
                return null;
        }
        if (!swissAlgorithm.isEven())
            results.add(MatchResult.WHITE_WON);

        return results;
    }

    private void refreshView(int goToRound, boolean nextRound){
        changeTextButton();
        matches = swissAlgorithm.getMatches().get(goToRound -1);
        if (nextRound){
            int count = 0;
            for (int i =0; i<matches.size() * 2; i+=2) {
                if (count == matches.size() - 1 && !swissAlgorithm.isEven()) {
                    textViews[i].setTextColor(Color.GREEN);
                    textViews[i + 1].setTextColor(Color.RED);
                }
                else {
                    textViews[i].setTextColor(getColor(R.color.colorPrimaryDark));
                    textViews[i + 1].setTextColor(getColor(R.color.colorPrimaryDark));
                }

                textViews[i].setText(matches.get(count).getPlayer1().toString());
                textViews[i + 1].setText(matches.get(count).getPlayer2().toString());
                initSpinner(spinners[count]);
                count++;
            }
        }
        else {
            Intent intent = new Intent(getApplicationContext(), RoundResults.class);
            intent.putExtra(getString(R.string.go_to_round), goToRound);
            startActivity(intent);
        }
    }





    private MatchResult getMatchResultFromSpinner(int pos){
        switch (spinners[pos].getSelectedItemPosition()){
            case 0:
                return MatchResult.WHITE_WON;
            case 1:
                return MatchResult.DRAW;
            case 2:
                return MatchResult.BLACK_WON;
        }
        return null; //not selected result
    }

    private void initSpinnersAndTextViews(){

        textViews = new TextView[matches.size() * 2];
        spinners = new Spinner[matches.size()];

        //matches
        for (int i =0;i<matches.size() * 2; i++){
            TextView textView = new TextView(this);
            textView.setId(i);
            textView.setTextColor(getColor(R.color.colorPrimaryDark));
            textViews[i] = textView;
        }

        //spinners
        for (int i = 0; i<matches.size(); i++){
            Spinner spinner = new Spinner(this);
            spinner.setId(i);
            spinners[i] = spinner;

        }
    }


    private void changeTextButton(){
        if (SwissAlgorithm.getINSTANCE().getCurrentRound() == SwissAlgorithm.getINSTANCE().getRoundsNumber()){
            nextRoundButton.setText(getString(R.string.show_results_button));
        }
    }

    private void buildRoundsView(){

        changeTextButton();

        matches = swissAlgorithm.getMatches().get(swissAlgorithm.getCurrentRound() - 1);

        initSpinnersAndTextViews();
        titleTextView.setText(getString(R.string.round_count_text_view, swissAlgorithm.getCurrentRound()));



        LinearLayout matchesRelativeLayout = findViewById(R.id.linear_layout_matches);

        //title
        titleTextView.setTextSize(30.0f);
        titleTextView.setTypeface(titleTextView.getTypeface(), Typeface.BOLD);
        titleTextView.setText(getString(R.string.round_count_text_view, swissAlgorithm.getCurrentRound()));
        titleTextView.setGravity(Gravity.CENTER_HORIZONTAL);



        LinearLayout.LayoutParams paramsLeftTextSize = new LinearLayout.LayoutParams(0, 50);
        paramsLeftTextSize.weight = 1;
        paramsLeftTextSize.setMargins(0, 5, 0, 5);

        LinearLayout.LayoutParams paramsSpinner = new LinearLayout.LayoutParams(0, 50);
        paramsSpinner.weight = 1;
        paramsSpinner.setMargins(50, 5, 0, 5);

        LinearLayout.LayoutParams paramsRightTextSize = new LinearLayout.LayoutParams(0, 50);
        paramsRightTextSize.weight = 1;
        paramsRightTextSize.setMargins(0, 5, 0, 5);



        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(10,5,10,5);
        linearLayout.setLayoutParams(params);
        matchesRelativeLayout.addView(linearLayout);

        int matchNumber = 0;
        for (int i =0; i<matches.size() * 2;i+=2){
            LinearLayout l = new LinearLayout(this);
            l.setOrientation(LinearLayout.HORIZONTAL);

            textViews[i].setLayoutParams(paramsLeftTextSize);
            textViews[i].setGravity(Gravity.START);
            textViews[i].setText(matches.get(matchNumber).getPlayer1().toString());
            textViews[i].setAutoSizeTextTypeUniformWithConfiguration(1, 30, 2, TypedValue.COMPLEX_UNIT_SP);
            l.addView(textViews[i]);

            if (matchNumber == matches.size() - 1 && !swissAlgorithm.isEven()){

                textViews[i].setTextColor(Color.GREEN);
                TextView textView = new TextView(this);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);

                LinearLayout.LayoutParams paramsBye = new LinearLayout.LayoutParams(0, 50);
                paramsBye.weight = 1;
                paramsBye.setMargins(0, 5, 0, 5);

                textView.setLayoutParams(paramsBye);
                textView.setText(getString(R.string.white_won_result));
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(getColor(R.color.colorPrimaryDark));
                textViews[i + 1].setTextColor(Color.RED);
                l.addView(textView);
            }
            else {
                initSpinner(spinners[matchNumber]);
                spinners[matchNumber].setLayoutParams(paramsSpinner);
                spinners[matchNumber].setGravity(Gravity.CENTER);
                l.addView(spinners[matchNumber]);
            }

            textViews[i + 1].setGravity(Gravity.END);
            textViews[i + 1].setText(matches.get(matchNumber).getPlayer2().toString());
            textViews[i + 1].setLayoutParams(paramsRightTextSize);
            textViews[i + 1].setAutoSizeTextTypeUniformWithConfiguration(1, 30, 2, TypedValue.COMPLEX_UNIT_SP);

            l.addView(textViews[i + 1]);

            linearLayout.addView(l);


            matchNumber++;
        }


    }

    private void initSpinner(final Spinner spinner) {

        List<String> list = new ArrayList<>(Arrays.asList(this.getResources().getStringArray(R.array.results_array)));
        list.add(getString(R.string.set_results)); //add hint
        SpinnerAdapter adapter = new SpinnerAdapter(this, R.layout.spinner_result, list);

        adapter.setDropDownViewResource(R.layout.spinner_result);
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getCount());

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (parent.getSelectedItem() != getString(R.string.set_results))
                            spinners[spinner.getId()].setSelection( parent.getSelectedItemPosition() );
                        colorWinner(spinner.getId());


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void colorWinner(int matchNumber){
        switch (spinners[matchNumber].getSelectedItemPosition()){
            case 0: //WHITE_WON
                textViews[matchNumber * 2].setTextColor(Color.GREEN);
                textViews[matchNumber * 2 + 1].setTextColor(Color.RED);
                break;
            case 1: //DRAW
                textViews[matchNumber * 2].setTextColor(Color.BLUE);
                textViews[matchNumber * 2 + 1].setTextColor(Color.BLUE);
                break;
            case 2: //BLACK_WON
                textViews[matchNumber * 2 + 1].setTextColor(Color.GREEN);
                textViews[matchNumber * 2].setTextColor(Color.RED);
                break;

        }
    }
    @Override
    public void onOkClicked(GeneralDialogFragment dialog) {
        if (dialog.getTag().equals(getString(R.string.title_warning))) {
            Intent i = new Intent(getApplicationContext(), CreateTournament.class);
            startActivity(i);
        }
    }

    @Override
    public void onCancelClicked(GeneralDialogFragment dialog) {

    }
}
