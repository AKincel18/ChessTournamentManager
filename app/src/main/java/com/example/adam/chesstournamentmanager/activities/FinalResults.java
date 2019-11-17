package com.example.adam.chesstournamentmanager.activities;

import android.content.Intent;
import android.content.res.Resources;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.adam.chesstournamentmanager.R;
import com.example.adam.chesstournamentmanager.SwissAlgorithm;
import com.example.adam.chesstournamentmanager.model.TournamentPlayer;

import java.util.List;
import java.util.Locale;

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
        if (item.getItemId() == currentRound && !SwissAlgorithm.getINSTANCE().isFinishedTournament()){
            Intent i = new Intent(getApplicationContext(), Tournament.class);
            startActivity(i);
        } else if (item.getItemId() <= currentRound) {
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



    private void buildHeader(){
        LinearLayout.LayoutParams paramsBuchholzHeader = new LinearLayout.LayoutParams(250 ,ViewGroup.LayoutParams.MATCH_PARENT);
        paramsBuchholzHeader.setMargins(0,0,0,5);

        TextView buchholzHeaderTextView = new TextView(this);
        buchholzHeaderTextView.setTextColor(Color.BLACK);
        buchholzHeaderTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        buchholzHeaderTextView.setGravity(Gravity.START);
        buchholzHeaderTextView.setLayoutParams(paramsBuchholzHeader);

        if (SwissAlgorithm.getINSTANCE().getPlaceOrder() == 0) {
            buchholzHeaderTextView.setText(getString(R.string.buchholz_header_text_view));
        } else {
            buchholzHeaderTextView.setText(getString(R.string.median_buchholz_header_text_view));
        }

        LinearLayout headerLayout = findViewById(R.id.header_results_layout);
        headerLayout.addView(buchholzHeaderTextView);
    }
    private void buildView() {

        LinearLayout matchesRelativeLayout = findViewById(R.id.linear_layout_matches);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(10,5,10,5);
        linearLayout.setLayoutParams(params);
        matchesRelativeLayout.addView(linearLayout);


        LinearLayout.LayoutParams paramsPosition = new LinearLayout.LayoutParams(100, 50);
        paramsPosition.setMargins(0, 5, 0, 5);

        LinearLayout.LayoutParams paramsPlayerTextView = new LinearLayout.LayoutParams(300, 50);
        paramsPlayerTextView.setMargins(0, 5, 0, 5);

        LinearLayout.LayoutParams paramsPointsTextView = new LinearLayout.LayoutParams(250, 50);
        paramsPointsTextView.setMargins(0, 5, 0, 5);

        if (SwissAlgorithm.getINSTANCE().isFinishedTournament()) {
            buildHeader();
        }

        List<TournamentPlayer> playerList = SwissAlgorithm.getINSTANCE().getPlayers();


        for (int i=0; i< playerList.size(); i++){
            LinearLayout l = new LinearLayout(this);
            l.setOrientation(LinearLayout.HORIZONTAL);

            TextView positionTextView = new TextView(this);
            positionTextView.setTextSize(30);
            positionTextView.setTextColor(Color.BLACK);
            positionTextView.setGravity(Gravity.START);
            positionTextView.setText(getString(R.string.lp, (i + 1)));
            positionTextView.setLayoutParams(paramsPosition);
            l.addView(positionTextView);

            TextView playerTextView = new TextView(this);
            playerTextView.setTextSize(30);
            playerTextView.setTextColor(Color.BLACK);
            playerTextView.setGravity(Gravity.START);
            playerTextView.setText(playerList.get(i).toString());
            playerTextView.setLayoutParams(paramsPlayerTextView);

            if (i == 0){
                playerTextView.setTextColor(getColor(R.color.colorGolden));
                playerTextView.setTypeface(null, Typeface.BOLD);
            }
            if (i == 1) {
                playerTextView.setTextColor(getColor(R.color.colorSilver));
                playerTextView.setTypeface(null, Typeface.BOLD);
            }
            if (i == 2) {
                playerTextView.setTextColor(getColor(R.color.colorBronze));
                playerTextView.setTypeface(null, Typeface.BOLD);
            }

            l.addView(playerTextView);


            TextView pointTextView = new TextView(this);
            pointTextView.setTextSize(30);
            pointTextView.setTextColor(Color.BLACK);
            pointTextView.setGravity(Gravity.START);
            pointTextView.setLayoutParams(paramsPointsTextView);

            pointTextView.setText(String.format(new Locale(getString(R.string.locale)),getString(R.string.format_float), playerList.get(i).getPoints()));
            //pointTextView.setText(Float.toString(playerList.get(i).getPoints()));
            l.addView(pointTextView);


            if (SwissAlgorithm.getINSTANCE().isFinishedTournament()) {


                TextView buchholzPointsTextView = new TextView(this);
                buchholzPointsTextView.setTextSize(30);
                buchholzPointsTextView.setTextColor(Color.BLACK);
                buchholzPointsTextView.setGravity(Gravity.START);
                buchholzPointsTextView.setLayoutParams(paramsPointsTextView);

                if (SwissAlgorithm.getINSTANCE().getPlaceOrder() == 0) {
                    buchholzPointsTextView.setText(String.format(new Locale(getString(R.string.locale)),getString(R.string.format_float), playerList.get(i).getBuchholzPoints()));
                } else {
                    buchholzPointsTextView.setText(String.format(new Locale(getString(R.string.locale)), getString(R.string.format_float), playerList.get(i).getMedianBuchholzMethod()));
                }

                l.addView(buchholzPointsTextView);
            }

            linearLayout.addView(l);



        }
    }

}
