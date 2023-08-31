package com.example.salt.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.salt.R;


public class DeleteRecordingDialog extends DialogFragment {
    public static final int DELETE_REQUEST_CODE = 0;

    public static DeleteRecordingDialog newInstance(Fragment targetFragment) {
        DeleteRecordingDialog dialog = new DeleteRecordingDialog();
        dialog.setTargetFragment(targetFragment, DELETE_REQUEST_CODE);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.delete_dialog_title)
                .setNegativeButton(R.string.cancel_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        getTargetFragment().onActivityResult(DELETE_REQUEST_CODE, Activity.RESULT_CANCELED, intent);
                    }
                })
                .setPositiveButton(R.string.proceed_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        getTargetFragment().onActivityResult(DELETE_REQUEST_CODE, Activity.RESULT_OK, intent);
                    }
                });
        return builder.show();
    }
}
