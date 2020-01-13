package com.example.adam.chesstournamentmanager.staticdata.dialogbox;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.example.adam.chesstournamentmanager.R;
import com.example.adam.chesstournamentmanager.staticdata.Constants;

public class GeneralDialogFragment extends BaseDialogFragment<OnDialogFragmentClickListener> {

    public static GeneralDialogFragment newInstance(String title, String message, String positiveBtn) {
        GeneralDialogFragment fragment = new GeneralDialogFragment();
        Bundle args = new Bundle();
        args.putString(Constants.TITLE, title);
        args.putString(Constants.MESSAGE, message);
        args.putString(Constants.POSITIVE_BTN, positiveBtn);
        fragment.setArguments(args);
        return fragment;
    }

    public static GeneralDialogFragment exitDialogBox() {
        return GeneralDialogFragment.newInstance(
                Constants.WARNING_TITLE, Constants.EXIT_MESSAGE, Constants.POSITIVE_BUTTON);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.alertDialog);
        builder.setTitle(getArguments().getString(Constants.TITLE))
                .setMessage(getArguments().getString(Constants.MESSAGE))
                .setPositiveButton(getArguments().getString(Constants.POSITIVE_BTN),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Positive button clicked
                                getActivityInstance().onOkClicked(GeneralDialogFragment.this);
                            }
                        }

                );

        //build 'no' button
        if (getArguments().getString(Constants.TITLE).equals(getString(R.string.title_warning)) ||
                getArguments().getString(Constants.TITLE).equals(getString(R.string.remove_player_title_DB)))
            builder.setNegativeButton(getString(R.string.negative_button_warning),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // negative button clicked
                            getActivityInstance().onCancelClicked(GeneralDialogFragment.this);
                        }
                    }
            );
        return builder.create();
    }


}