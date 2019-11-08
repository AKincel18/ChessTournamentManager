package com.example.adam.chesstournamentmanager.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.adam.chesstournamentmanager.Match;
import com.example.adam.chesstournamentmanager.R;
import com.example.adam.chesstournamentmanager.SwissAlgorithm;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private static int N = 0;

    private List<Match> matches;

    private int currentRound;

    private TextView titleTextView;

    public PlaceholderFragment() {
    }

/*    @SuppressLint("ValidFragment")
    public PlaceholderFragment(SwissAlgorithm swissAlgorithm) {
        this.swissAlgorithm = swissAlgorithm;
        this.currentRound = 1;
    }*/

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber, SwissAlgorithm swissAlgorithm) {

        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putParcelable("swiss", swissAlgorithm);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tournament_process, container, false);

            int section = getArguments().getInt(ARG_SECTION_NUMBER);

            SwissAlgorithm swissAlgorithm = getArguments().getParcelable(getString(R.string.swiss));
            titleTextView = rootView.findViewById(R.id.roundCountTextView);
            if (section > (currentRound = swissAlgorithm.getCurrentRound())){

                if ( section == swissAlgorithm.getRoundsNumber() + 1) { // results fragment
                    resultBuildView(swissAlgorithm.getRoundsNumber());
                    return rootView;
                }
                //previous rounds have no finished
                noCurrentRoundBuildView();
                return rootView;
            }
            else {
                swissAlgorithm.drawFirstRound();
                matches = swissAlgorithm.getMatches().get(currentRound - 1);

                if (getArguments().getInt(ARG_SECTION_NUMBER) == 1)
                    currentRoundBuildView(rootView);
                System.out.println("onCreateView");


               return rootView;
            }

    }

    private void resultBuildView(int roundNumber){
        titleTextView.setTextSize(30);
        titleTextView.setTypeface(titleTextView.getTypeface(), Typeface.BOLD);
        titleTextView.setGravity(Gravity.CENTER_HORIZONTAL);

        if (currentRound == 1) {
            titleTextView.setText(getString(R.string.results_here));
        } else if (currentRound == roundNumber) {
            titleTextView.setText(getString(R.string.final_result));
        } else {
            titleTextView.setText(getString(R.string.current_results,currentRound));
        }




    }
    private void noCurrentRoundBuildView() {

        titleTextView.setTextSize(50);
        titleTextView.setTypeface(titleTextView.getTypeface(), Typeface.BOLD);
        titleTextView.setText(getString(R.string.not_finished_round));
        titleTextView.setGravity(Gravity.CENTER_HORIZONTAL);
    }

    private void currentRoundBuildView(View view){
        LinearLayout relativeLayout = view.findViewById(R.id.linearLayoutFragment);


        LinearLayout matchesRelativeLayout = view.findViewById(R.id.linearLayoutMatches);

        //title
        titleTextView.setTextSize(30.0f);
        titleTextView.setTypeface(titleTextView.getTypeface(), Typeface.BOLD);
        titleTextView.setText(getString(R.string.fragmentNumber, currentRound));
        titleTextView.setGravity(Gravity.CENTER_HORIZONTAL);



    LinearLayout linearLayout = new LinearLayout(getContext());
    linearLayout.setOrientation(LinearLayout.VERTICAL);
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    params.setMargins(10,5,10,5);
    linearLayout.setLayoutParams(params);
    matchesRelativeLayout.addView(linearLayout);
    int count = 0;
    LinearLayout.LayoutParams paramsLeftTextSize = new LinearLayout.LayoutParams(200, 50);
    paramsLeftTextSize.setMargins(5, 5, 50, 5);

    LinearLayout.LayoutParams paramsSpinner = new LinearLayout.LayoutParams(200, 50);
    paramsSpinner.setMargins(200, 5, 100, 5);

    LinearLayout.LayoutParams paramsRightTextSize = new LinearLayout.LayoutParams(200, 50);
    paramsRightTextSize.setMargins(50, 5, 10, 5);



        //matches
        TextView[] textViews = new TextView[matches.size() * 2];
        for (int i =0;i<matches.size() * 2; i++){
            TextView textView = new TextView(getContext());
            textView.setId(i);
            textViews[i] = textView;
        }

        for (int i =0; i<matches.size() * 2;i+=2){
            LinearLayout l = new LinearLayout(getContext());
            l.setOrientation(LinearLayout.HORIZONTAL);


            Spinner spinner = new Spinner(getContext());
            initSpinner(spinner);

            textViews[i].setTextSize(20);
            textViews[i].setLayoutParams(paramsLeftTextSize);
            textViews[i].setText(matches.get(count).getPlayer1().toString());
            textViews[i].setId(View.generateViewId());
            textViews[i].setAutoSizeTextTypeUniformWithPresetSizes(getResources().getIntArray(R.array.autosize_text_sizes),
                    TypedValue.COMPLEX_UNIT_SP );
            l.addView(textViews[i]);

            spinner.setLayoutParams(paramsSpinner);
            l.addView(spinner);

            textViews[i + 1].setTextSize(20);
            textViews[i + 1].setGravity(Gravity.END);
            textViews[i + 1].setText(matches.get(count).getPlayer2().toString());
            textViews[i + 1].setId(View.generateViewId());
            textViews[i + 1].setLayoutParams(paramsRightTextSize);
            textViews[i + 1].setAutoSizeTextTypeUniformWithPresetSizes(getResources().getIntArray(R.array.autosize_text_sizes),
                    TypedValue.COMPLEX_UNIT_SP );
            l.addView(textViews[i + 1], new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            linearLayout.addView(l);

            count++;
        }


    }

    private void initSpinner(final Spinner spinner){

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.results_array, R.layout.spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setId(View.generateViewId());

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (parent.getSelectedItemPosition()){
                    case 0:
                        System.out.println("1-0");
                        break;
                    case 1:
                        System.out.println("1/2");
                        break;
                    case 2:
                        System.out.println("0-1");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
