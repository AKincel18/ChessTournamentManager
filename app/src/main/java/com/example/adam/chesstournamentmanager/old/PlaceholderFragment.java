package com.example.adam.chesstournamentmanager.old;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.adam.chesstournamentmanager.Match;
import com.example.adam.chesstournamentmanager.MatchResult;
import com.example.adam.chesstournamentmanager.R;
import com.example.adam.chesstournamentmanager.SwissAlgorithm;
import com.example.adam.chesstournamentmanager.staticdata.Constans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

    private List<Match> matches;

    private int currentRound;

    private TextView titleTextView;

    private Button nextRoundButton;

    private SwissAlgorithm swissAlgorithm;

    private TextView[] textViews;

    private  Spinner[] spinners;

    private List<Fragment> fragments;

    static private int section;



    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */

    public static PlaceholderFragment newInstance(int sectionNumber, SwissAlgorithm swissAlgorithm, List<Fragment> fragments) {

        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(Constans.ARG_SECTION_NUMBER, sectionNumber);
        args.putSerializable(Constans.SWISS, swissAlgorithm);
        args.putSerializable("aaa", (Serializable) fragments);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tournament_process, container, false);

            section = getArguments().getInt(Constans.ARG_SECTION_NUMBER);





        System.out.println("on create view = " + section);

        swissAlgorithm = (SwissAlgorithm) getArguments().getSerializable(getString(R.string.swiss));
        currentRound = swissAlgorithm.getCurrentRound();
        fragments = (List<Fragment>) getArguments().getSerializable("aaa");
        nextRoundButton = rootView.findViewById(R.id.nextRoundButton);
        titleTextView = rootView.findViewById(R.id.roundCountTextView);
        nextRoundButton();


        if (section == 1){
            swissAlgorithm.drawFirstRound();
            matches = swissAlgorithm.getMatches().get(currentRound - 1);
            textViews = new TextView[matches.size() * 2];
            spinners = new Spinner[matches.size()];
            currentRoundBuildView(rootView);
        }
        /*//nextRoundButton();
            if (section == 1){
                //init(rootView);
                swissAlgorithm.drawFirstRound();
                matches = swissAlgorithm.getMatches().get(currentRound - 1);
                textViews = new TextView[matches.size() * 2];
                spinners = new Spinner[matches.size()];
                currentRoundBuildView(rootView);
            }
            else if (section == currentRound){
                swissAlgorithm.drawNextRound();
                matches = swissAlgorithm.getMatches().get(currentRound - 1);
                textViews = new TextView[matches.size() * 2];
                spinners = new Spinner[matches.size()];
                currentRoundBuildView(rootView);

            }
            else if (section > currentRound){
                if ( section == swissAlgorithm.getRoundsNumber() + 1) { // results fragment
                    resultBuildView(swissAlgorithm.getRoundsNumber());
                    return rootView;
                }
                //previous rounds have no finished
                noCurrentRoundBuildView();
            }*/

            return rootView;
    }
/*

    private void init(View rootView){
        swissAlgorithm = (SwissAlgorithm) getArguments().getSerializable(getString(R.string.swiss));
        currentRound = swissAlgorithm.getCurrentRound();
        fragments = (List<Fragment>) getArguments().getSerializable("aaa");
        nextRoundButton = rootView.findViewById(R.id.nextRoundButton);
        titleTextView = rootView.findViewById(R.id.roundCountTextView);

        nextRoundButton();
    }
*/

    private void resultBuildView(int roundNumber){
        nextRoundButton.setVisibility(View.INVISIBLE);
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            System.out.println("hint = " + section);
        }
    }

    private void noCurrentRoundBuildView() {
        nextRoundButton.setVisibility(View.INVISIBLE);
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
    LinearLayout.LayoutParams paramsLeftTextSize = new LinearLayout.LayoutParams(200, 50);
    paramsLeftTextSize.setMargins(5, 5, 50, 5);

    LinearLayout.LayoutParams paramsSpinner = new LinearLayout.LayoutParams(200, 50);
    paramsSpinner.setMargins(200, 5, 100, 5);

    LinearLayout.LayoutParams paramsRightTextSize = new LinearLayout.LayoutParams(200, 50);
    paramsRightTextSize.setMargins(50, 5, 10, 5);



        //matches
        for (int i =0;i<matches.size() * 2; i++){
            TextView textView = new TextView(getContext());
            textView.setId(i);
            textViews[i] = textView;
        }

        //spinners
        for (int i = 0; i<matches.size(); i++){
            Spinner spinner = new Spinner(getContext());
            spinner.setId(i);
            spinners[i] = spinner;

        }

        int count = 0;
        for (int i =0; i<matches.size() * 2;i+=2){
            LinearLayout l = new LinearLayout(getContext());
            l.setOrientation(LinearLayout.HORIZONTAL);


            initSpinner(spinners[count]);

            textViews[i].setTextSize(20);
            textViews[i].setLayoutParams(paramsLeftTextSize);
            textViews[i].setText(matches.get(count).getPlayer1().toString());
            textViews[i].setId(View.generateViewId());
            textViews[i].setAutoSizeTextTypeUniformWithPresetSizes(getResources().getIntArray(R.array.autosize_text_sizes),
                    TypedValue.COMPLEX_UNIT_SP );
            l.addView(textViews[i]);

            spinners[count].setLayoutParams(paramsSpinner);
            l.addView(spinners[count]);

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

    private void nextRoundButton(){
        nextRoundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pociagnij i zapisz wyniki
                swissAlgorithm.setResult(getResult());
                //wylosuj nastepne kojarzenia
                swissAlgorithm.drawNextRound();

                //otworz nastepna karte
                //openNextFragment();
            }
        });
    }


    private void openNextFragment(){

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragments.get(currentRound));
        transaction.addToBackStack(null);
        transaction.commit();

    }

    private List<MatchResult> getResult(){
        List<MatchResult> results = new ArrayList<>();
        for (int i=0; i < matches.size(); i++){
            results.add(getMatchResultFromSpinner(i));
        }
        return results;
    }

    private MatchResult getMatchResultFromSpinner(int pos){
        switch (spinners[pos].getSelectedItemPosition()){
            case 0:
                return MatchResult.WHITE_WON;
            case 1:
                return MatchResult.DRAW;
            case 2:
                return MatchResult.BLACK_WON;
        }
        return null;
    }
    private void initSpinner(final Spinner spinner){

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.results_array, R.layout.spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                spinners[spinner.getId()].setSelection( parent.getSelectedItemPosition() );
                /*switch (parent.getSelectedItemPosition()){
                    case 0:

                        System.out.println("1-0");
                        break;
                    case 1:
                        System.out.println("1/2");
                        break;
                    case 2:
                        System.out.println("0-1");
                        break;
                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
