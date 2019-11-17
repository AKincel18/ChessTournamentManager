package com.example.adam.chesstournamentmanager.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.adam.chesstournamentmanager.Match;
import com.example.adam.chesstournamentmanager.R;
import com.example.adam.chesstournamentmanager.SwissAlgorithm;

import java.util.List;

public class RoundResults extends AppCompatActivity {

    private Menu myMenu;

    private List<List<Match>> allMatches;

    private int currentRound;

    private int currentView;

    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_results);
        Intent i = getIntent();
/*        allMatches = (List<List<Match>>) i.getSerializableExtra("matches");
        currentRound = i.getIntExtra(getString(R.string.current_round), 0);*/
        allMatches = SwissAlgorithm.getINSTANCE().getMatches();
        currentRound = SwissAlgorithm.getINSTANCE().getCurrentRound();
        currentView = i.getIntExtra(getString(R.string.go_to_round), 0);
        textView = findViewById(R.id.previous_round_count_text_view);
        textView.setText(getString(R.string.round_count_text_view, currentView));
        buildView(currentView);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == currentRound && !SwissAlgorithm.getINSTANCE().isFinishedTournament()){
            Intent i = new Intent(getApplicationContext(), Tournament.class);
            startActivity(i);
        }
        else if (item.getItemId() <= currentRound) {

            LinearLayout matchesRelativeLayout = findViewById(R.id.linear_layout_matches);
            matchesRelativeLayout.removeAllViews();
            buildView(item.getItemId());
        }
        else if (item.getItemId() ==  R.id.results_menu){
            Intent i = new Intent(getApplicationContext(), FinalResults.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        myMenu = menu;
        getMenuInflater().inflate(R.menu.menu_tournament, myMenu);
        buildMenu();
        return true;
    }

    private void buildMenu() {
        MenuItem menuItem = myMenu.findItem(R.id.rounds_menu);
        SubMenu subMenu = menuItem.getSubMenu();
        for (int i = 1; i <= currentRound; i++){
            subMenu.add(Menu.NONE,i, Menu.NONE,getString(R.string.round_count_text_view, i));
        }
    }

    private void buildView(int currentRound){
        textView.setText(getString(R.string.round_count_text_view, currentRound));

        LinearLayout matchesRelativeLayout = findViewById(R.id.linear_layout_matches);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(10,5,10,5);
        linearLayout.setLayoutParams(params);
        matchesRelativeLayout.addView(linearLayout);

        LinearLayout.LayoutParams paramsLp = new LinearLayout.LayoutParams(75, 50);
        paramsLp.setMargins(0, 5, 5, 5);


        LinearLayout.LayoutParams paramsPlayer1 = new LinearLayout.LayoutParams(250, 50);
        paramsPlayer1.setMargins(0, 5, 0, 5);

        LinearLayout.LayoutParams paramsResult = new LinearLayout.LayoutParams(150, 50);
        paramsResult.setMargins(15, 5, 0, 5);

        List<Match> matches = allMatches.get(currentRound - 1);

        for (int i = 0; i < matches.size(); i++){
            LinearLayout l = new LinearLayout(this);
            l.setOrientation(LinearLayout.HORIZONTAL);

            TextView lpTextView = new TextView(this);
            lpTextView.setText(getString(R.string.lp, (i + 1)));
            lpTextView.setTextSize(30);
            lpTextView.setGravity(Gravity.START);
            lpTextView.setTextColor(Color.BLACK);
            lpTextView.setLayoutParams(paramsLp);
            l.addView(lpTextView);

            TextView player1TextView = new TextView(this);
            player1TextView.setText(matches.get(i).getPlayer1().toString());
            player1TextView.setTextSize(30);
            player1TextView.setGravity(Gravity.START);
            player1TextView.setLayoutParams(paramsPlayer1);
            l.addView(player1TextView);

            TextView player2TextView = new TextView(this);
            player2TextView.setText(matches.get(i).getPlayer2().toString());
            player2TextView.setTextSize(30);
            player2TextView.setGravity(Gravity.START);
            player2TextView.setLayoutParams(paramsPlayer1);
            l.addView(player2TextView);



            TextView resultTextView = new TextView(this);
            resultTextView.setText(getMatchResult(player1TextView, player2TextView, matches.get(i)));
            resultTextView.setTextSize(30);
            resultTextView.setGravity(Gravity.START);
            resultTextView.setTextColor(Color.BLACK);
            resultTextView.setLayoutParams(paramsResult);
            l.addView(resultTextView);


            linearLayout.addView(l);
        }

    }

    private String getMatchResult(TextView p1, TextView p2, Match match){
        switch (match.getMatchResult()){
            case WHITE_WON:
                p1.setTextColor(Color.GREEN);
                p2.setTextColor(Color.RED);
                return getString(R.string.white_won_result);
            case DRAW:
                p1.setTextColor(Color.BLUE);
                p2.setTextColor(Color.BLUE);
                return getString(R.string.draw_result);
            case BLACK_WON:
                p1.setTextColor(Color.RED);
                p2.setTextColor(Color.GREEN);
                return getString(R.string.black_won_result);
        }
        return null;
    }
}
