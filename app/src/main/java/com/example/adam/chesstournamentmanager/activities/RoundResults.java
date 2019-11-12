package com.example.adam.chesstournamentmanager.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
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

        LinearLayout.LayoutParams paramsTextSize = new LinearLayout.LayoutParams(200, 50);
        paramsTextSize.setMargins(5, 5, 50, 5);

        LinearLayout.LayoutParams paramsResultTextSize = new LinearLayout.LayoutParams(200, 50);
        paramsResultTextSize.setMargins(50, 5, 10, 5);

        List<Match> matches = allMatches.get(currentRound - 1);

        for (int i = 0; i < matches.size(); i++){
            LinearLayout l = new LinearLayout(this);
            l.setOrientation(LinearLayout.HORIZONTAL);

            TextView playersTextView = new TextView(this);
            playersTextView.setText(matches.get(i).writeMatch());
            playersTextView.setTextSize(20);
            playersTextView.setLayoutParams(paramsTextSize);
            playersTextView.setAutoSizeTextTypeUniformWithPresetSizes(getResources().getIntArray(R.array.autosize_text_sizes),
                    TypedValue.COMPLEX_UNIT_SP );

            l.addView(playersTextView);


            TextView resultTextView = new TextView(this);
            resultTextView.setText(matches.get(i).writeResult());
            resultTextView.setTextSize(20);
            resultTextView.setLayoutParams(paramsResultTextSize);
            resultTextView.setAutoSizeTextTypeUniformWithPresetSizes(getResources().getIntArray(R.array.autosize_text_sizes),
                    TypedValue.COMPLEX_UNIT_SP );

            l.addView(resultTextView);


            linearLayout.addView(l);
        }

    }
}
