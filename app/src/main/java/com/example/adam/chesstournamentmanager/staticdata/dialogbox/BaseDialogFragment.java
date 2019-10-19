package com.example.adam.chesstournamentmanager.staticdata.dialogbox;

import android.content.Context;
import android.support.v4.app.DialogFragment;

public abstract class BaseDialogFragment<T> extends DialogFragment {
    private T mActivityInstance;

    public final T getActivityInstance() {
        return mActivityInstance;
    }

    @Override
    public void onAttach(Context context) {
        mActivityInstance = (T) context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivityInstance = null;
    }
}
