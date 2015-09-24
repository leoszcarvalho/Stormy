package com.carvalho.leonardo.stormy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by Leonardo on 23/09/2015.
 */
public class AlertDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Context context = getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(R.string.oops)
                .setMessage(R.string.error_mesg)
                .setPositiveButton(R.string.ok_msg, null);

        AlertDialog dialog = builder.create();

        return dialog;

    }
}
