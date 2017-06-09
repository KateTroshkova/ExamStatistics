package com.troshkova.portfolioprogect.examsapp.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import com.troshkova.portfolioprogect.examsapp.R;

public class InfoDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.info));
        builder.setMessage(getString(R.string.info_text));
        return builder.create();
    }
}