package com.example.adam.chesstournamentmanager.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.adam.chesstournamentmanager.R;
import com.example.adam.chesstournamentmanager.SwissAlgorithm;
import com.example.adam.chesstournamentmanager.model.TournamentPlayer;

import java.util.List;

public class FinalResults extends AppCompatActivity {

    private int currentRound;

    private Menu myMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_results);
        currentRound = SwissAlgorithm.getINSTANCE().getCurrentRound();
        TextView textView = findViewById(R.id.current_results);
        if (SwissAlgorithm.getINSTANCE().isFinishedTournament()) {
            textView.setText(getString(R.string.final_result));
        }
        else {
            textView.setText(getString(R.string.current_results, (currentRound - 1)));
        }

        buildView();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        myMenu = menu;
        getMenuInflater().inflate(R.menu.menu_tournament, myMenu);
        buildMenu();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == currentRound){
            Intent i = new Intent(getApplicationContext(), Tournament.class);
            startActivity(i);
        } else if (item.getItemId() < currentRound) {
            Intent i = new Intent(getApplicationContext(), RoundResults.class);
            i.putExtra(getString(R.string.go_to_round), item.getItemId());
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    private void buildMenu() {
        MenuItem menuItem = myMenu.findItem(R.id.rounds_menu);
        SubMenu subMenu = menuItem.getSubMenu();
        for (int i = 1; i <= currentRound; i++){
            subMenu.add(Menu.NONE,i, Menu.NONE,getString(R.string.round_count_text_view, i));
        }
    }



    private void buildView() {

        LinearLayout matchesRelativeLayout = findViewById(R.id.linear_layout_matches);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(10,5,10,5);
        linearLayout.setLayoutParams(params);
        matchesRelativeLayout.addView(linearLayout);


        LinearLayout.LayoutParams paramsPlayerTextView = new LinearLayout.LayoutParams(200, 50);
        paramsPlayerTextView.setMargins(5, 5, 50, 5);

        LinearLayout.LayoutParams paramsPointsTextView = new LinearLayout.LayoutParams(350, 50);
        paramsPointsTextView.setMargins(50, 5, 100, 5);

        List<TournamentPlayer> playerList = SwissAlgorithm.getINSTANCE().getPlayers();


        for (int i=0; i< playerList.size(); i++){
            LinearLayout l = new LinearLayout(this);
            l.setOrientation(LinearLayout.HORIZONTAL);

            TextView playerTextView = new TextView(this);
            playerTextView.setTextSize(20);
            playerTextView.setTextColor(Color.BLACK);
            playerTextView.setLayoutParams(paramsPlayerTextView);

            String text = (i + 1) + ". " + playerList.get(i).toString();
            playerTextView.setText(text);

            playerTextView.setId(View.generateViewId());
            playerTextView.setAutoSizeTextTypeUniformWithPresetSizes(getResources().getIntArray(R.array.autosize_text_sizes),
                    TypedValue.COMPLEX_UNIT_SP );
            l.addView(playerTextView);


            TextView pointTextView = new TextView(this);
            pointTextView.setTextSize(20);
            pointTextView.setTextColor(Color.BLACK);
            pointTextView.setLayoutParams(paramsPointsTextView);


            String points = Float.toString(playerList.get(i).getPoints()) + " pkt";
            pointTextView.setText(points);

            pointTextView.setId(View.generateViewId());
            pointTextView.setAutoSizeTextTypeUniformWithPresetSizes(getResources().getIntArray(R.array.autosize_text_sizes),
                    TypedValue.COMPLEX_UNIT_SP );
            l.addView(pointTextView);

            linearLayout.addView(l);



        }
    }

}
