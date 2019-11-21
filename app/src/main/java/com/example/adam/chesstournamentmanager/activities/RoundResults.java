package com.example.adam.chesstournamentmanager.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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

import com.example.adam.chesstournamentmanager.matches.Match;
import com.example.adam.chesstournamentmanager.R;
import com.example.adam.chesstournamentmanager.swissalgorithm.SwissAlgorithm;
import com.example.adam.chesstournamentmanager.staticdata.dialogbox.GeneralDialogFragment;
import com.example.adam.chesstournamentmanager.staticdata.dialogbox.OnDialogFragmentClickListener;

import java.util.List;

public class RoundResults extends AppCompatActivity implements OnDialogFragmentClickListener {

    private Menu myMenu;

    private List<List<Match>> allMatches;

    private int currentRound;

    private TextView textView;

    private ActionBarDrawerToggle actionBarDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_results);
        Intent i = getIntent();
        allMatches = SwissAlgorithm.getINSTANCE().getMatches();
        currentRound = SwissAlgorithm.getINSTANCE().getCurrentRound();
        int currentView = i.getIntExtra(getString(R.string.go_to_round), 0);
        textView = findViewById(R.id.previous_round_count_text_view);
        textView.setText(getString(R.string.round_count_text_view, currentView));


        initNavigationMenu();
        buildMenu();
        buildView(currentView);

    }

    private void initNavigationMenu(){

        final DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);


        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.nav_view);

        myMenu = navigationView.getMenu();


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() != R.id.exit_menu) {
                    if (item.getItemId() == currentRound && !SwissAlgorithm.getINSTANCE().isFinishedTournament()) {
                        Intent i = new Intent(getApplicationContext(), Tournament.class);
                        startActivity(i);
                    } else if (item.getItemId() <= currentRound) {

                        LinearLayout matchesRelativeLayout = findViewById(R.id.linear_layout_matches);
                        matchesRelativeLayout.removeAllViews();
                        buildView(item.getItemId());
                        drawerLayout.closeDrawer(GravityCompat.START);
                    } else if (item.getItemId() == R.id.results_menu) {
                        Intent i = new Intent(getApplicationContext(), FinalResults.class);
                        startActivity(i);
                    }
                } else {
                    GeneralDialogFragment dialog = GeneralDialogFragment.exitDialogBox();
                    dialog.show(getSupportFragmentManager(),  getString(R.string.title_warning));
                }
                return true;
            }
        });
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

        LinearLayout.LayoutParams paramsLp = new LinearLayout.LayoutParams(0, 50, 0.05f);
        paramsLp.setMargins(0, 5, 5, 5);


        LinearLayout.LayoutParams paramsPlayer1 = new LinearLayout.LayoutParams(0, 50, 0.35f);
        paramsPlayer1.setMargins(0, 5, 5, 5);

        LinearLayout.LayoutParams paramsResult = new LinearLayout.LayoutParams(0, 50, 0.25f);
        paramsResult.setMargins(15, 5, 0, 5);

        List<Match> matches = allMatches.get(currentRound - 1);

        for (int i = 0; i < matches.size(); i++){
            LinearLayout l = new LinearLayout(this);
            l.setOrientation(LinearLayout.HORIZONTAL);

            TextView lpTextView = new TextView(this);
            lpTextView.setText(getString(R.string.lp, (i + 1)));
            lpTextView.setAutoSizeTextTypeUniformWithConfiguration(1, 30, 1, TypedValue.COMPLEX_UNIT_SP);
            lpTextView.setGravity(Gravity.START);
            lpTextView.setTextColor(getColor(R.color.colorPrimaryDark));
            lpTextView.setLayoutParams(paramsLp);
            l.addView(lpTextView);

            TextView player1TextView = new TextView(this);
            player1TextView.setText(matches.get(i).getPlayer1().toString());
            player1TextView.setAutoSizeTextTypeUniformWithConfiguration(1, 30, 1, TypedValue.COMPLEX_UNIT_SP);
            player1TextView.setGravity(Gravity.START);
            player1TextView.setLayoutParams(paramsPlayer1);
            l.addView(player1TextView);

            TextView player2TextView = new TextView(this);
            player2TextView.setText(matches.get(i).getPlayer2().toString());
            player2TextView.setAutoSizeTextTypeUniformWithConfiguration(1, 30, 1, TypedValue.COMPLEX_UNIT_SP);
            player2TextView.setGravity(Gravity.START);
            player2TextView.setLayoutParams(paramsPlayer1);
            l.addView(player2TextView);



            TextView resultTextView = new TextView(this);
            resultTextView.setText(getMatchResult(player1TextView, player2TextView, matches.get(i)));
            resultTextView.setAutoSizeTextTypeUniformWithConfiguration(1, 30, 1, TypedValue.COMPLEX_UNIT_SP);
            resultTextView.setGravity(Gravity.START);
            resultTextView.setTextColor(getColor(R.color.colorPrimaryDark));
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

    @Override
    public void onOkClicked(GeneralDialogFragment dialog) {
            Intent i = new Intent(getApplicationContext(), CreateTournament.class);
            startActivity(i);
    }

    @Override
    public void onCancelClicked(GeneralDialogFragment dialog) {

    }
}
