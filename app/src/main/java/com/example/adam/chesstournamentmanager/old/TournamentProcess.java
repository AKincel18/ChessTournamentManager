package com.example.adam.chesstournamentmanager.old;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.adam.chesstournamentmanager.R;
import com.example.adam.chesstournamentmanager.SwissAlgorithm;
import com.example.adam.chesstournamentmanager.model.Players;

import java.util.List;

public class TournamentProcess extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private int roundsNumber;

    private List<Players> players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_process);

        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        Intent i = getIntent();
        roundsNumber = i.getIntExtra(getString(R.string.rounds_number), 0);
        String order = i.getStringExtra(getString(R.string.order));
        players = (List<Players>) i.getSerializableExtra(getString(R.string.players));


        SwissAlgorithm swissAlgorithm = new SwissAlgorithm(roundsNumber, order);
        swissAlgorithm.initTournamentPlayers(players);
        //swissAlgorithm.drawFirstRound();

        mSectionsPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager(), roundsNumber + 1, swissAlgorithm);


        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final TabLayout tabLayout = findViewById(R.id.tabs);


        tabLayout.setupWithViewPager(mViewPager);


        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));



        initView(tabLayout);

    }


    private void initView(TabLayout tabLayout){


        for (int a = 0; a < roundsNumber; a++) {
            mSectionsPagerAdapter.addFragment(new Fragment(), a );
            mSectionsPagerAdapter.notifyDataSetChanged();
        }
        mSectionsPagerAdapter.addFragment(new Fragment(), roundsNumber );
        mSectionsPagerAdapter.notifyDataSetChanged();

/*        for (int b = 0; b < roundsNumber; b++){
            tabLayout.getTabAt(b).setText(getString(R.string.fragmentNumber, b+1));
        }

        tabLayout.getTabAt(roundsNumber).setText(getString(R.string.results));*/
    }

}
