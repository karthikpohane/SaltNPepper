package com.example.salt.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


import com.example.salt.MyUtils;
import com.example.salt.R;

import java.io.IOException;

public class RecordPlayerDialog extends DialogFragment {
    public static final String ARG_FILE_PATH = "com.example.obidi.audiorecorder.filepath";
    public static final int DIALOG_REQUEST_CODE = 1;

    private View mView;
    private TextView mTxtTimePlayed;
    private ImageButton mPlayPauseButton;
    private SeekBar mSeekBar;
    private MediaPlayer mMediaPlayer;
    private Handler mHandler;
    private RecordingPlayerTask mRecordingPlayerTask;
    private String timerPlaceholder;
    private String mFilePath;
    private boolean mIsPlaying;

    // static factory function for creating a new instance of this class
    public static RecordPlayerDialog newInstance(String filePath) {
        RecordPlayerDialog playerDialog = new RecordPlayerDialog();
        Bundle args = new Bundle();
        args.putString(ARG_FILE_PATH, filePath);
        playerDialog.setArguments(args);
        return playerDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true); // survive configuration change
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        timerPlaceholder = getContext().getString(R.string.timer_placeholder);
        mFilePath = getArguments().getString(ARG_FILE_PATH);
        if (mFilePath != null) {
            mView = LayoutInflater.from(getContext())
                                .inflate(R.layout.fragment_sound_player_dialog, null);
            mPlayPauseButton = mView.findViewById(R.id.player_action_button);
            mPlayPauseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mIsPlaying) {
                        Toast.makeText(getContext(), "paused", Toast.LENGTH_SHORT).show();
                        mIsPlaying = false;
                        mMediaPlayer.pause();
                        mPlayPauseButton.setImageResource(R.mipmap.ic_play);
                    } else {
                        Toast.makeText(getContext(), "played", Toast.LENGTH_SHORT).show();
                        mIsPlaying = true;
                        mMediaPlayer.start();
                        mPlayPauseButton.setImageResource(R.mipmap.ic_pause);
                    }
                }
            });
            // handler should be initialized in the main/ui thread (thread it needs to update)
            mHandler = new Handler();
            mSeekBar = mView.findViewById(R.id.player_seekbar_bar);
            builder.setView(mView);
            return builder.show(); // return dialog
        } else {// return default implementation (i.e. don't create dialog)
            return super.onCreateDialog(savedInstanceState);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mRecordingPlayerTask = new RecordingPlayerTask();
        mRecordingPlayerTask.execute();
    }

    @Override
    public void onPause() {
        super.onPause();
        mRecordingPlayerTask.stop();
    }

    private class RecordingPlayerTask extends AsyncTask<Void, Void, Void> {
        private int mTotalDuration; // holds total seconds of the recording
        private int mTimePlayed = 0; // holds the seconds played
        private int previousMediaPlayerPosition;
        private boolean mStopTask;

        // stops terminates the task
        public void stop() {
            mStopTask = true;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mMediaPlayer = new MediaPlayer();
            try {
                /*
                Initialize mMediaPlayer, mIsPlaying, mTotalDuration,
                 */
                mMediaPlayer.setDataSource(mFilePath);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
                mIsPlaying = true;
                mTotalDuration = mMediaPlayer.getDuration();
                /*
                wire up the dialog view widgets
                 */
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        String[] totalTimeTC = MyUtils.formatTimeComponents(MyUtils
                                .getTimeComponents(mTotalDuration));
                        TextView txtTotalDuration = mView.findViewById(R.id.player_total_progress);
                        mTxtTimePlayed = mView.findViewById(R.id.player_current_progress);
                        txtTotalDuration.setText(formatTimerComps(totalTimeTC[1], totalTimeTC[2]));
                        mSeekBar.setMax(mTotalDuration);
                    }
                });
                while ((mTimePlayed < mTotalDuration) && !mStopTask) {
                    if (!mIsPlaying) continue;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final int currentMediaPosition = mMediaPlayer.getCurrentPosition();
                    final String[] timePlayedTC
                            = MyUtils.formatTimeComponents(MyUtils.getTimeComponents(currentMediaPosition));
                    mTimePlayed += 1000;
                    // increment progressBar using mHandler
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mTxtTimePlayed.setText(formatTimerComps(timePlayedTC[1], timePlayedTC[2]));
                            if (currentMediaPosition == previousMediaPlayerPosition) {
                                // end of recording reached
                                mSeekBar.setProgress(mTotalDuration);
                                stop();
                            } else {
                                mSeekBar.setProgress(currentMediaPosition);
                                previousMediaPlayerPosition = currentMediaPosition;
                            }
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally { // free up memory resources and close this dialog
                mMediaPlayer.release();
                mMediaPlayer = null;
                RecordPlayerDialog.this.dismiss();
            }
            return null;
        }
    }

    private String formatTimerComps(String min, String sec) {
        return String.format(timerPlaceholder, min, sec);
    }
}
