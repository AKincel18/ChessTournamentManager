package com.example.adam.chesstournamentmanager.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
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
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.adam.chesstournamentmanager.R;
import com.example.adam.chesstournamentmanager.swissalgorithm.SwissAlgorithm;
import com.example.adam.chesstournamentmanager.model.TournamentPlayer;
import com.example.adam.chesstournamentmanager.staticdata.dialogbox.GeneralDialogFragment;
import com.example.adam.chesstournamentmanager.staticdata.dialogbox.OnDialogFragmentClickListener;

import java.util.List;
import java.util.Locale;

public class FinalResults extends AppCompatActivity implements OnDialogFragmentClickListener {

    private int currentRound;

    private Menu myMenu;

    private ActionBarDrawerToggle actionBarDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_results);
        currentRound = SwissAlgorithm.getINSTANCE().getCurrentRound();
        TextView textView = findViewById(R.id.current_results);

        if (SwissAlgorithm.getINSTANCE().isFinishedTournament()) {
            textView.setText(getString(R.string.final_result));
        } else {
            textView.setText(getString(R.string.current_results, (currentRound - 1)));
        }

        initNavigationView();
        buildMenu();
        buildView();

    }

    private void initNavigationView() {

        final DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.nav_view);

        myMenu = navigationView.getMenu();
        buildColorMenu(R.id.rounds_menu, R.style.titleMenuStyle);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id != R.id.exit_menu) {
                    if (id == currentRound && !SwissAlgorithm.getINSTANCE().isFinishedTournament()) {
                        Intent i = new Intent(getApplicationContext(), Tournament.class);
                        startActivity(i);
                    } else if (id <= currentRound) {
                        Intent i = new Intent(getApplicationContext(), RoundResults.class);
                        i.putExtra(getString(R.string.go_to_round), id);
                        startActivity(i);
                    } else if (id == R.id.results_menu) {
                        drawerLayout.closeDrawer(Gravity.START);
                    }
                } else {
                    GeneralDialogFragment dialog = GeneralDialogFragment.exitDialogBox();
                    dialog.show(getSupportFragmentManager(), getString(R.string.title_warning));
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
        dialog.show(getSupportFragmentManager(), getString(R.string.title_warning));
    }

    private void buildMenu() {
        MenuItem menuItem = myMenu.findItem(R.id.rounds_menu);
        SubMenu subMenu = menuItem.getSubMenu();
        for (int i = 1; i <= currentRound; i++) {
            subMenu.add(Menu.NONE, i, Menu.NONE, getString(R.string.round_count_text_view, i));
            buildColorMenu(i, R.style.subMenuRoundsStyle);
        }
    }

    private void buildColorMenu(int rId, int rStyle) {
        MenuItem rounds = myMenu.findItem(rId);
        SpannableString s = new SpannableString(rounds.getTitle());
        s.setSpan(new TextAppearanceSpan(this, rStyle), 0, s.length(), 0);
        rounds.setTitle(s);
    }


    private void buildHeader() {
        LinearLayout.LayoutParams paramsBuchholzHeader = new LinearLayout.LayoutParams(250, ViewGroup.LayoutParams.MATCH_PARENT);
        paramsBuchholzHeader.setMargins(0, 0, 0, 5);

        TextView buchholzHeaderTextView = new TextView(this);
        buchholzHeaderTextView.setTextColor(getResources().getColor(R.color.colorAccent));
        buchholzHeaderTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        buchholzHeaderTextView.setGravity(Gravity.START);
        buchholzHeaderTextView.setLayoutParams(paramsBuchholzHeader);

        if (SwissAlgorithm.getINSTANCE().getPlaceOrder()) {
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
        params.setMargins(10, 5, 10, 5);
        linearLayout.setLayoutParams(params);
        matchesRelativeLayout.addView(linearLayout);


        LinearLayout.LayoutParams paramsPosition = new LinearLayout.LayoutParams(100, 50);
        paramsPosition.setMargins(0, 5, 0, 5);

        LinearLayout.LayoutParams paramsPlayerTextView = new LinearLayout.LayoutParams(450, 50);
        paramsPlayerTextView.setMargins(0, 5, 5, 5);

        LinearLayout.LayoutParams paramsPointsTextView = new LinearLayout.LayoutParams(250, 50);
        paramsPointsTextView.setMargins(0, 5, 0, 5);

        if (SwissAlgorithm.getINSTANCE().isFinishedTournament()) {
            buildHeader();
        }

        List<TournamentPlayer> playerList = SwissAlgorithm.getINSTANCE().getPlayers();


        for (int i = 0; i < playerList.size(); i++) {
            LinearLayout l = new LinearLayout(this);
            l.setOrientation(LinearLayout.HORIZONTAL);

            TextView positionTextView = new TextView(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                positionTextView.setAutoSizeTextTypeUniformWithConfiguration(1, 30, 2, TypedValue.COMPLEX_UNIT_SP);
            } else {
                positionTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f);
            }
            positionTextView.setTextColor(getResources().getColor(R.color.colorAccent));
            positionTextView.setGravity(Gravity.START);
            positionTextView.setText(getString(R.string.no, (i + 1)));
            positionTextView.setLayoutParams(paramsPosition);
            l.addView(positionTextView);

            TextView playerTextView = new TextView(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                playerTextView.setAutoSizeTextTypeUniformWithConfiguration(1, 30, 2, TypedValue.COMPLEX_UNIT_SP);
            } else {
                playerTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f);
            }
            playerTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            playerTextView.setGravity(Gravity.START);
            playerTextView.setText(playerList.get(i).toString());
            playerTextView.setLayoutParams(paramsPlayerTextView);

            if (i == 0) {
                playerTextView.setTextColor(getResources().getColor(R.color.colorGolden));
                playerTextView.setTypeface(null, Typeface.BOLD);
            }
            if (i == 1) {
                playerTextView.setTextColor(getResources().getColor(R.color.colorSilver));
                playerTextView.setTypeface(null, Typeface.BOLD);
            }
            if (i == 2) {
                playerTextView.setTextColor(getResources().getColor(R.color.colorBronze));
                playerTextView.setTypeface(null, Typeface.BOLD);
            }

            l.addView(playerTextView);


            TextView pointTextView = new TextView(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                pointTextView.setAutoSizeTextTypeUniformWithConfiguration(1, 30, 2, TypedValue.COMPLEX_UNIT_SP);
            } else {
                pointTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f);
            }
            pointTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            pointTextView.setGravity(Gravity.START);
            pointTextView.setLayoutParams(paramsPointsTextView);

            pointTextView.setText(String.format(new Locale(getString(R.string.locale)), getString(R.string.format_float), playerList.get(i).getPoints()));
            l.addView(pointTextView);


            if (SwissAlgorithm.getINSTANCE().isFinishedTournament()) {

                TextView buchholzPointsTextView = new TextView(this);
                buchholzPointsTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                buchholzPointsTextView.setGravity(Gravity.START);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    buchholzPointsTextView.setAutoSizeTextTypeUniformWithConfiguration(1, 30, 2, TypedValue.COMPLEX_UNIT_SP);
                } else {
                    buchholzPointsTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f);
                }
                buchholzPointsTextView.setLayoutParams(paramsPointsTextView);

                if (SwissAlgorithm.getINSTANCE().getPlaceOrder()) {
                    buchholzPointsTextView.setText(String.format(new Locale(getString(R.string.locale)), getString(R.string.format_float), playerList.get(i).getBuchholzPoints()));
                } else {
                    buchholzPointsTextView.setText(String.format(new Locale(getString(R.string.locale)), getString(R.string.format_float), playerList.get(i).getMedianBuchholzMethod()));
                }

                l.addView(buchholzPointsTextView);
            }

            linearLayout.addView(l);


        }
    }

    @Override
    public void onOkClicked(GeneralDialogFragment dialog) {
        Intent i = new Intent(getApplicationContext(), PlayersSelection.class);
        startActivity(i);
    }

    @Override
    public void onCancelClicked(GeneralDialogFragment dialog) {

    }
}
