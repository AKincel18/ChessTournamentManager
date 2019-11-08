package com.example.adam.chesstournamentmanager.staticdata.dialogbox;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.adam.chesstournamentmanager.R;
import com.example.adam.chesstournamentmanager.staticdata.Constans;

public class GeneralDialogFragment extends BaseDialogFragment<GeneralDialogFragment.OnDialogFragmentClickListener> {

    // interface to handle the dialog click back to the Activity
    public interface OnDialogFragmentClickListener {
        void onOkClicked(GeneralDialogFragment dialog);
        void onCancelClicked(GeneralDialogFragment dialog);
    }

    // Create an instance of the Dialog with the input
    public static GeneralDialogFragment newInstance(String title, String message, String possitiveBtn) {
        GeneralDialogFragment frag = new GeneralDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("msg", message);
        args.putString("possitiveBtn", possitiveBtn);
        frag.setArguments(args);
        return frag;
    }
    // Create a Dialog using default AlertDialog builder , if not inflate custom view in onCreateView
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder .setTitle(getArguments().getString("title"))
                .setMessage(getArguments().getString("msg"))
                .setPositiveButton(getArguments().getString("possitiveBtn"),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Positive button clicked
                        getActivityInstance().onOkClicked(GeneralDialogFragment.this);
                    }
                }

        );

        if (getArguments().getString("title").equals(getString(R.string.title_warning)))
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
