package com.example.adam.chesstournamentmanager.old;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.adam.chesstournamentmanager.SwissAlgorithm;

import java.util.ArrayList;
import java.util.List;

public class SectionPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private int fragmentCount;
    private SwissAlgorithm swissAlgorithm;

    public SectionPagerAdapter(FragmentManager fm, int fragmentCount, SwissAlgorithm swissAlgorithm) {
        super(fm);
        this.fragmentCount = fragmentCount;
        this.swissAlgorithm = swissAlgorithm;
    }

    @Override
    public Fragment getItem(int position) {
        return PlaceholderFragment.newInstance(position + 1, swissAlgorithm, mFragmentList);
    }

    @Override
    public int getCount() {
        return fragmentCount;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position + 1!= fragmentCount)
            return "Runda " + (position + 1);
        else
            return "Wyniki";
    }

    public void addFragment(Fragment fragment, int position) {
        mFragmentList.add(position, fragment);
    }

/*    public void removeFragment(int position) {
        mFragmentList.remove(position);
    }*/
}
