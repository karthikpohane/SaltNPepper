package com.example.salt;

import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecorderViewModel extends ViewModel {
    private Handler mTimerHandler;
    private TimerTask mTimerTask;
    private MediaRecorder mRecorder;
    private File mOutputFile;

    private MutableLiveData<Boolean> mIsRecording = new MutableLiveData<>();
    private MutableLiveData<Boolean> mIsRecordingPaused = new MutableLiveData<>();
    private MutableLiveData<Integer> mTimerDuration = new MutableLiveData<>();

    public RecorderViewModel() {
        mIsRecording.setValue(false);
        mIsRecordingPaused.setValue(false);
        mTimerDuration.setValue(0);
    }

    public MutableLiveData<Boolean> getIsRecording() {
        return mIsRecording;
    }

    public MutableLiveData<Boolean> getIsRecordingPaused() {
        return mIsRecordingPaused;
    }

    public MutableLiveData<Integer> getTimerDuration() {
        return mTimerDuration;
    }

    public void startRecording() {
        File recordDir = new File (Environment.getExternalStorageDirectory().getAbsolutePath(),
                Environment.DIRECTORY_PODCASTS);
        mOutputFile = new File(recordDir, getFileName());
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mRecorder.setOutputFile(mOutputFile.getAbsolutePath());

        try {
            mRecorder.prepare(); // prepare the mRecorder for recording
            mRecorder.start(); // start recording
            runTimer(); // run
            mIsRecording.setValue(true); // trigger main/ui update

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void resumeRecording(){
        /**
         * if the current sdk version is below 24
         * retrieve the fileName of the previous main file recording
         * create a temporary file which will hold the recording
         * prepare the mRecorder object and start recording to the temporary file
         * when stop / pause is called,
         * merge the temporary file with the previous main file recording
         * Else if the sdk version is 24 and above
         * call mRecorder.resume()
         */
        mIsRecordingPaused.setValue(false); // trigger main/ui update
        mIsRecording.setValue(true);
    }

    public void pauseRecording() {
        /**
         * If the current sdk version is below 24
         * stop the mRecorder object
         * indicate that the user called pause on the recording
         * Else if the sdk version is 24 and above
         * call mRecorder.pause()
         */
        mIsRecordingPaused.setValue(true); // trigger main/ui update
        mIsRecording.setValue(false);
    }

    public File stopRecording() {
        if (mRecorder != null) {
            mRecorder.stop();
            //mCallbacks.onRecordingSaved(); // notify host activity of saved recording
            if (mTimerTask != null) mTimerTask.stop();
            // stops the mediaRecorder instance
            mIsRecording.setValue(false); // trigger main/ui update
            mIsRecordingPaused.setValue(false);
            mRecorder.release();
            return mOutputFile;
        }
        return null;
    }

    public void runTimer() {
        mTimerHandler = new Handler();
        mTimerTask = new TimerTask();
        new Thread(mTimerTask).start();
    }

    public class TimerTask implements Runnable {
        private boolean mStopTimerTask;
        @Override
        public void run() {
            int seconds = 0;
            while (!mStopTimerTask) {
                try {
                    Thread.sleep(1000);
                    final int dummyInt = seconds ++;
                    mTimerHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mTimerDuration.setValue(dummyInt); // trigger main/ui timer text update
                        }
                    });
                } catch (InterruptedException iex) {
                    iex.printStackTrace();
                }
            }
        }
        void stop() {
            mStopTimerTask = true;
        }
    }

    private String getFileName() {
        return "record_" + new SimpleDateFormat("yyyy-MM-dd-HH-mm").format(new Date()) + ".mp3";
    }
}

