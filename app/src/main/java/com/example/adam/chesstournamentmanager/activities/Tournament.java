package com.example.adam.chesstournamentmanager.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.example.adam.chesstournamentmanager.Match;
import com.example.adam.chesstournamentmanager.MatchResult;
import com.example.adam.chesstournamentmanager.R;
import com.example.adam.chesstournamentmanager.SwissAlgorithm;
import com.example.adam.chesstournamentmanager.model.Players;
import com.example.adam.chesstournamentmanager.staticdata.SpinnerAdapter;
import com.example.adam.chesstournamentmanager.staticdata.dialogbox.GeneralDialogFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tournament extends AppCompatActivity implements GeneralDialogFragment.OnDialogFragmentClickListener{

    private TextView[] textViews;

    private  Spinner[] spinners;

    private SwissAlgorithm swissAlgorithm;

    private TextView titleTextView;

    private List<Match> matches;

    private int roundsNumber;

    private Button nextRoundButton;

    private Menu myMenu;

    private int currentView;

    private boolean rebuildMenu = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tournament);
        nextRoundButton = findViewById(R.id.next_round_button);
        titleTextView = findViewById(R.id.round_count_text_view);

        swissAlgorithm = SwissAlgorithm.getINSTANCE();
        if (swissAlgorithm != null){
            rebuildMenu = true;
            roundsNumber = swissAlgorithm.getRoundsNumber();
        }
        else {

            Intent i = getIntent();
            roundsNumber = i.getIntExtra(getString(R.string.rounds_number), 0);
            String order = i.getStringExtra(getString(R.string.order));
            List<Players> players = (List<Players>) i.getSerializableExtra(getString(R.string.players));

            swissAlgorithm = SwissAlgorithm.initSwissAlgorithm(roundsNumber, order);
            swissAlgorithm.initTournamentPlayers(players);
            swissAlgorithm.drawFirstRound();
        }


        buildRoundsView();
        nextRoundButton();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        myMenu = menu;
        getMenuInflater().inflate(R.menu.menu_tournament, myMenu);
        if (rebuildMenu){
            rebuildMenu();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.results_menu && swissAlgorithm.getCurrentRound() != 1){
            Intent i = new Intent(getApplicationContext(), FinalResults.class);
            startActivity(i);
        } else if (item.getItemId() >= 1 && item.getItemId() <= swissAlgorithm.getRoundsNumber()){
            refreshView(item.getItemId(), false);
        }
        return super.onOptionsItemSelected(item);
    }

    private void nextRoundButton(){
        nextRoundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<MatchResult> matchResults = getResult();
                if (matchResults != null) {
                    swissAlgorithm.setResult(matchResults);
                }
                else {
                    notAllResultsEnteredDialogBox();
                }

                buildMenu();
                if (swissAlgorithm.isFinishedTournament()) {
                    Intent i = new Intent(getApplicationContext(), FinalResults.class);
                    startActivity(i);
                }
                else {
                        swissAlgorithm.drawNextRound();
                        initTitle(swissAlgorithm.getCurrentRound());
                        refreshView(swissAlgorithm.getCurrentRound(), true);
                    }




            }
        });
    }

    private void notAllResultsEnteredDialogBox(){
        GeneralDialogFragment generalDialogFragment =
                GeneralDialogFragment.newInstance(getString(R.string.title_error), getString(R.string.no_result_message_error), getString(R.string.exit_button));
        generalDialogFragment.show(getSupportFragmentManager(), getString(R.string.title_warning));
    }

    private void buildMenu() {
        MenuItem menuItem = myMenu.findItem(R.id.rounds_menu);
        SubMenu subMenu = menuItem.getSubMenu();
        if (swissAlgorithm.isFinishedTournament())
            subMenu.add(Menu.NONE,swissAlgorithm.getCurrentRound(), Menu.NONE,getString(R.string.round_count_text_view, swissAlgorithm.getCurrentRound()));
        else
            subMenu.add(Menu.NONE,swissAlgorithm.getCurrentRound() - 1, Menu.NONE,getString(R.string.round_count_text_view, swissAlgorithm.getCurrentRound() - 1));

    }

    private void rebuildMenu(){
        MenuItem menuItem = myMenu.findItem(R.id.rounds_menu);
        SubMenu subMenu = menuItem.getSubMenu();
        for (int i =1; i <= swissAlgorithm.getCurrentRound() - 1; i++){
            subMenu.add(Menu.NONE,i, Menu.NONE,getString(R.string.round_count_text_view, i));
        }
    }

    private List<MatchResult> getResult(){
        int matchCount;
        if (swissAlgorithm.isEven())
            matchCount = matches.size();
        else
            matchCount = matches.size() - 1;

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

        matches = swissAlgorithm.getMatches().get(goToRound -1);

        if (nextRound){
            int count = 0;
            for (int i =0; i<matches.size() * 2; i+=2) {
                textViews[i].setText(matches.get(count).getPlayer1().toString());
                textViews[i + 1].setText(matches.get(count).getPlayer2().toString());
                initSpinner(spinners[count]);
                count++;
            }
        }
        else {

            Intent intent = new Intent(getApplicationContext(), RoundResults.class);
/*            intent.putExtra("matches", (Serializable) swissAlgorithm.getMatches());
            intent.putExtra(getString(R.string.current_round), swissAlgorithm.getCurrentRound());*/
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

    private void init(){

        textViews = new TextView[matches.size() * 2];
        spinners = new Spinner[matches.size()];

        //matches
        for (int i =0;i<matches.size() * 2; i++){
            TextView textView = new TextView(this);
            textView.setId(i);
            textViews[i] = textView;
        }

        //spinners
        for (int i = 0; i<matches.size(); i++){
            Spinner spinner = new Spinner(this);
            spinner.setId(i);
            spinners[i] = spinner;

        }
    }

    private void initTitle(int round){
        //title
        titleTextView.setTextSize(30.0f);
        titleTextView.setTypeface(titleTextView.getTypeface(), Typeface.BOLD);
        titleTextView.setText(getString(R.string.round_count_text_view, round));
        titleTextView.setGravity(Gravity.CENTER_HORIZONTAL);
    }

    private void buildRoundsView(){


        matches = swissAlgorithm.getMatches().get(swissAlgorithm.getCurrentRound() - 1);

        init();
        initTitle(swissAlgorithm.getCurrentRound());

        LinearLayout relativeLayout = findViewById(R.id.linearLayoutFragment);


        LinearLayout matchesRelativeLayout = findViewById(R.id.linearLayoutMatches);

        //title
        titleTextView.setTextSize(30.0f);
        titleTextView.setTypeface(titleTextView.getTypeface(), Typeface.BOLD);
        titleTextView.setText(getString(R.string.round_count_text_view, swissAlgorithm.getCurrentRound()));
        titleTextView.setGravity(Gravity.CENTER_HORIZONTAL);



        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(10,5,10,5);
        linearLayout.setLayoutParams(params);
        matchesRelativeLayout.addView(linearLayout);


        LinearLayout.LayoutParams paramsLeftTextSize = new LinearLayout.LayoutParams(200, 50);
        paramsLeftTextSize.setMargins(5, 5, 50, 5);

        LinearLayout.LayoutParams paramsSpinner = new LinearLayout.LayoutParams(350, 50);
        paramsSpinner.setMargins(180, 5, 100, 5);

        LinearLayout.LayoutParams paramsRightTextSize = new LinearLayout.LayoutParams(200, 50);
        paramsRightTextSize.setMargins(50, 5, 10, 5);



        //matches
        for (int i =0;i<matches.size() * 2; i++){
            TextView textView = new TextView(this);
            textView.setId(i);
            textViews[i] = textView;
        }

        //spinners
        for (int i = 0; i<matches.size(); i++){
            Spinner spinner = new Spinner(this);
            spinner.setId(i);
            spinners[i] = spinner;
        }

        int count = 0;
        for (int i =0; i<matches.size() * 2;i+=2){
            LinearLayout l = new LinearLayout(this);
            l.setOrientation(LinearLayout.HORIZONTAL);




            textViews[i].setTextSize(20);
            textViews[i].setTextColor(Color.BLACK);
            textViews[i].setLayoutParams(paramsLeftTextSize);
            textViews[i].setText(matches.get(count).getPlayer1().toString());
            textViews[i].setId(View.generateViewId());
            textViews[i].setAutoSizeTextTypeUniformWithPresetSizes(getResources().getIntArray(R.array.autosize_text_sizes),
                    TypedValue.COMPLEX_UNIT_SP );
            l.addView(textViews[i]);

            if (count == matches.size() - 1 && !swissAlgorithm.isEven()){
                TextView textView = new TextView(this);
                textView.setTextSize(20);
/*                LinearLayout.LayoutParams paramsResult = new LinearLayout.LayoutParams(300, 50);
                paramsRightTextSize.setMargins(200, 5, 100, 5);
                textView.setLayoutParams(paramsResult);*/

                textView.setLayoutParams(paramsSpinner);
                textView.setText("1-0");
                textView.setId(View.generateViewId());
                textView.setAutoSizeTextTypeUniformWithPresetSizes(getResources().getIntArray(R.array.autosize_text_sizes),
                        TypedValue.COMPLEX_UNIT_SP );
                l.addView(textView);
            }
            else {
                    initSpinner(spinners[count]);
                spinners[count].setLayoutParams(paramsSpinner);
                //spinners[count].setMinimumWidth(400);
                //spinners[count].setDropDownWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                l.addView(spinners[count]);
            }
            textViews[i + 1].setTextSize(20);
            textViews[i + 1].setTextColor(Color.BLACK);
            textViews[i + 1].setGravity(Gravity.END);
            textViews[i + 1].setText(matches.get(count).getPlayer2().toString());
            textViews[i + 1].setId(View.generateViewId());
            textViews[i + 1].setLayoutParams(paramsRightTextSize);
            textViews[i + 1].setAutoSizeTextTypeUniformWithPresetSizes(getResources().getIntArray(R.array.autosize_text_sizes),
                    TypedValue.COMPLEX_UNIT_SP );
            l.addView(textViews[i + 1], new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            linearLayout.addView(l);

            count++;
        }






    }

    private void initSpinner(final Spinner spinner) {

        List<String> list = new ArrayList<>(Arrays.asList(this.getResources().getStringArray(R.array.results_array)));
        list.add(getString(R.string.set_results)); //add hint
        SpinnerAdapter adapter = new SpinnerAdapter(this, R.layout.spinner_item, list);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getCount());

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (parent.getSelectedItem() != getString(R.string.set_results))
                            spinners[spinner.getId()].setSelection( parent.getSelectedItemPosition() );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
