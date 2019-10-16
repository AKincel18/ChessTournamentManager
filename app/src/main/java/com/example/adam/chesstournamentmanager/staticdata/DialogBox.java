package com.example.adam.chesstournamentmanager.staticdata;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;

import com.example.adam.chesstournamentmanager.activities.CreateTournament;
import com.example.adam.chesstournamentmanager.R;

public class DialogBox extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(Constans.TTILE_DB)
                .setMessage(Constans.INFORMATION_DB)
                .setIcon(R.drawable.message_icon) //TODO not showing
                .setPositiveButton(Constans.POSITIVE_BUTTON_DB, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getActivity(), CreateTournament.class);
                        startActivity(i);
                    }
                })
                .setNegativeButton(Constans.NEGATIVE_BUTTON_DB, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }
}
