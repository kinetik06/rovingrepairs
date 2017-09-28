package com.zombietechinc.rovingrepairs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by User on 8/29/2017.
 */

public class ConfirmVehicleDialogFragment extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Great!")
                .setMessage("Let's Put that in your Garage!")
                .setPositiveButton(context.getString(R.string.error_ok_button_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });

        AlertDialog dialog = builder.create();
        return dialog;
    }


}