package com.example.salt;

import static java.security.AccessController.getContext;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.salt.dialog.SaveRecordingDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;

public class RecordFragment extends Fragment {
    private final int REQUEST_SAVE_RECORDING = 12;
    private final int RECORDING_RESUMED = 0;
    private final int RECORDING_STOPPED = 2;

    private String timerPlaceholder;
    private TextView mTimerView;
    private TextView mRecordingStatusLabel;
    private FloatingActionButton mPlayPauseButton;
    private FloatingActionButton mStopButton;

    private RecorderViewModel mViewModel;

    Callbacks mCallbacks;

    interface Callbacks {
        void onRecordingSaved();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SAVE_RECORDING) {
            if (resultCode == Activity.RESULT_OK) {
                mCallbacks.onRecordingSaved(); // update recording list
                Toast.makeText(getContext(), "Recording Saved", Toast.LENGTH_SHORT).show();
            } else {
                RecordLab recordLab = new RecordLab(getContext());
                boolean isDeleted = recordLab.deleteRecording(data.getStringExtra(SaveRecordingDialog.ARG_RECORD_OUTPUT_PATH));
                if (isDeleted) Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                else Toast.makeText(getContext(), "Not Deleted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RecorderViewModel.class);
    }

    @Override
    public void onStart() {
        super.onStart();
        mViewModel.getIsRecording().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isRecording) {
                if (isRecording) {
                    // recording ongoing and not paused
                    showPauseDrawable(true); // change button drawable to ic_pause
                    mStopButton.setVisibility(View.VISIBLE); // show stopButton
                    updateRecordingStatusText(RECORDING_RESUMED);
                } else if (!isRecording && !mViewModel.getIsRecordingPaused().getValue()) {
                    // recording stopped
                    mStopButton.setVisibility(View.GONE);
                    showPauseDrawable(false);
                    updateRecordingStatusText(RECORDING_STOPPED);
                }
            }
        });

        mViewModel.getIsRecordingPaused().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isPaused) {
                if (isPaused) {
                    showPauseDrawable(true);
                    updateRecordingStatusText(RECORDING_RESUMED);
                }
            }
        });

        mViewModel.getTimerDuration().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer seconds) {
                updateTimerText(seconds);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_record, container, false);
        mTimerView = v.findViewById(R.id.text_view_timer_countdown);
        timerPlaceholder = getString(R.string.timer_placeholder);
        mRecordingStatusLabel = v.findViewById(R.id.text_view_recording_status);
        mPlayPauseButton = v.findViewById(R.id.button_record);
        mStopButton = v.findViewById(R.id.button_stop);

        mPlayPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewModel.getIsRecordingPaused().getValue()) { // currently paused, resume recording
                    mViewModel.resumeRecording();
                } else if (mViewModel.getIsRecording().getValue()) { // currently resumed, pause recording
                    mViewModel.pauseRecording();
                } else { // start recording new audio
                    mViewModel.startRecording();
                }
            }
        });

        /*
        Here after the Recorder is stopped, the user will have to save the recording or it will be deleted
         */
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File recordOutputFile = mViewModel.stopRecording(); // stop recording audio
                SaveRecordingDialog saveDialog =
                        SaveRecordingDialog.newInstance(RecordFragment.this,
                                recordOutputFile.getAbsolutePath(), REQUEST_SAVE_RECORDING);
                saveDialog.show(getFragmentManager(), "Save Dialog");
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTimerText(mViewModel.getTimerDuration().getValue());
    }

    private void showPauseDrawable(boolean isPaused) {
        if (isPaused) mPlayPauseButton.setImageResource(R.mipmap.ic_pause);
        else mPlayPauseButton.setImageResource(R.mipmap.ic_record);
    }

    private void updateRecordingStatusText(int status) {
        switch (status) {
            case RECORDING_RESUMED: {
                mRecordingStatusLabel.setText(R.string.recording_status);
                break;
            }
            case RECORDING_STOPPED: {
                mRecordingStatusLabel.setText(R.string.not_recording_status);
                break;
            }
        }
    }

    private void updateTimerText(int seconds) {
        final String[] comps
                = MyUtils.formatTimeComponents(MyUtils.getTimeComponents(seconds * 1000));
        String timerText = String.format(timerPlaceholder, comps[1], comps[2]);
        mTimerView.setText(timerText);
    }
}
