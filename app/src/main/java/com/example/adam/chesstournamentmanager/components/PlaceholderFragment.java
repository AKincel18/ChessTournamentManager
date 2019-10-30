package com.example.adam.chesstournamentmanager.components;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.adam.chesstournamentmanager.R;
import com.example.adam.chesstournamentmanager.SwissAlgorithm;
import com.example.adam.chesstournamentmanager.model.TournamentPlayer;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private SwissAlgorithm swissAlgorithm;

    public PlaceholderFragment() {
    }

    @SuppressLint("ValidFragment")
    public PlaceholderFragment(SwissAlgorithm swissAlgorithm) {
        this.swissAlgorithm = swissAlgorithm;
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber, SwissAlgorithm swissAlgorithm) {

        PlaceholderFragment fragment = new PlaceholderFragment(swissAlgorithm);
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tournament_process, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

/*        for (TournamentPlayer p : swissAlgorithm.getPlayers()){
            System.out.println(p.toString());
        }*/


        return rootView;
    }
}
