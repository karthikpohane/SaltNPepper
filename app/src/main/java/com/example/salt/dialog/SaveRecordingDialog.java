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


public class SaveRecordingDialog extends DialogFragment {
    public static final String ARG_TARGET_FRAGMENT_REQUEST_CODE = "target fragment request code";
    public static final String ARG_RECORD_OUTPUT_PATH = "record output file";

    public static SaveRecordingDialog newInstance(Fragment targetFragment, String recordOutputPath, int requestCode) {
        SaveRecordingDialog dialog = new SaveRecordingDialog();
        dialog.setTargetFragment(targetFragment, requestCode);
        Bundle args = new Bundle();
        args.putString(ARG_RECORD_OUTPUT_PATH, recordOutputPath);
        args.putInt(ARG_TARGET_FRAGMENT_REQUEST_CODE, requestCode);
        dialog.setArguments(args);
        return dialog;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        final int requestCode = getArguments().getInt(ARG_TARGET_FRAGMENT_REQUEST_CODE, 0);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.save_recording)
                .setNegativeButton(R.string.cancel_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        getTargetFragment().onActivityResult(requestCode, Activity.RESULT_CANCELED, intent);
                    }
                })
                .setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.putExtra(ARG_RECORD_OUTPUT_PATH, getArguments().getString(ARG_RECORD_OUTPUT_PATH));
                        getTargetFragment().onActivityResult(requestCode, Activity.RESULT_OK, intent);
                    }
                });
        return builder.show();
    }
}
