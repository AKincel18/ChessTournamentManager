package com.example.adam.chesstournamentmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.adam.chesstournamentmanager.R;
import com.example.adam.chesstournamentmanager.components.SectionPagerAdapter;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_process);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        Intent i = getIntent();
        roundsNumber = i.getIntExtra("roundsNumber", 0);

        mSectionsPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager(), roundsNumber + 1);


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);


        tabLayout.setupWithViewPager(mViewPager);


        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));



        for (int a = 0; a < roundsNumber; a++) {
            mSectionsPagerAdapter.addFragment(new Fragment(), a );
            mSectionsPagerAdapter.notifyDataSetChanged();
        }

        mSectionsPagerAdapter.addFragment(new Fragment(), roundsNumber );
        mSectionsPagerAdapter.notifyDataSetChanged();

        for (int b = 0; b < roundsNumber; b++){
            tabLayout.getTabAt(b).setText(getString(R.string.fragmentNumber, Integer.valueOf(b+1)));
        }

        tabLayout.getTabAt(roundsNumber).setText(getString(R.string.results));


    }


}
