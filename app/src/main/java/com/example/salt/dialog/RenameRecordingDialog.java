package com.example.salt.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.salt.R;


public class RenameRecordingDialog extends DialogFragment {
    public static final int RENAME_REQUEST_CODE = 2;
    public static final String EXTRA_NEW_NAME = "new recording name";
    public static final String BUNDLE_RECORDING_FILE_NAME = "recording name";

    public static RenameRecordingDialog newInstance(Fragment targetFragment, String prevString) {
        RenameRecordingDialog dialog = new RenameRecordingDialog();
        dialog.setTargetFragment(targetFragment, RENAME_REQUEST_CODE);
        Bundle args = new Bundle();
        args.putString(BUNDLE_RECORDING_FILE_NAME, prevString);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.rename_recording_dialog, null);
        final EditText editText = view.findViewById(R.id.editText_rename_recording);
        final String previousName = getArguments().getString(BUNDLE_RECORDING_FILE_NAME);
        editText.setText(previousName);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view)
                .setTitle(R.string.rename_recording)
                .setNegativeButton(R.string.cancel_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        String newName = editText.getEditableText().toString();
                        if (newName.isEmpty() || newName.equals(previousName)) {
                            getTargetFragment().onActivityResult(RENAME_REQUEST_CODE, Activity.RESULT_CANCELED, intent);
                        }
                    }
                })
                .setPositiveButton(R.string.proceed_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();

                        String newName = editText.getEditableText().toString();
                        if (!newName.isEmpty() || !(newName.equals(previousName))) {
                            intent.putExtra(EXTRA_NEW_NAME, newName);
                            getTargetFragment().onActivityResult(RENAME_REQUEST_CODE, Activity.RESULT_OK, intent);
                        }
                    }
                });
        return builder.show();
    }
}
