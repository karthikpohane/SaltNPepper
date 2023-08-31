package com.example.salt;

import android.content.Context;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.salt.dialog.RecordPlayerDialog;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class RecordingListAdapter extends RecyclerView.Adapter<RecordingListAdapter.RecordingHolder> {
    private Context mContext;
    private FragmentActivity mActivity;
    private RecordingListFragment mTargetFragment;

    private List<RecordLab.Recording> mRecordings;

    public RecordingListAdapter(RecordingListFragment soundListFragment,
                                List<RecordLab.Recording> recordings) {
        mContext = soundListFragment.getContext();
        mActivity = soundListFragment.getActivity();
        mRecordings = recordings;
        mTargetFragment = soundListFragment;
    }

    @NonNull
    @Override
    public RecordingListAdapter.RecordingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.sound_item, parent, false);
        return new RecordingHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordingListAdapter.RecordingHolder holder, int position) {
        holder.bindRecording(mRecordings.get(position));
    }

    @Override
    public int getItemCount() {
        return mRecordings.size();
    }

    public void setRecordings(List<RecordLab.Recording> recordings) {
        mRecordings = recordings;
    }

    class RecordingHolder extends RecyclerView.ViewHolder {
        private RecordLab.Recording mRecording;

        RecordingHolder(final View itemView) {
            super(itemView);
            mActivity.registerForContextMenu(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RecordPlayerDialog dialog = RecordPlayerDialog.newInstance(mRecording.getFilePath());
                    dialog.setTargetFragment(mTargetFragment, RecordPlayerDialog.DIALOG_REQUEST_CODE);
                    mTargetFragment.getFragmentManager()
                            .beginTransaction()
                            .add(dialog, "Media Player Dialog")
                            .commit();
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mTargetFragment.setSelectedRecording(mRecording);
                    mActivity.openContextMenu(itemView);
                    return true;
                }
            });
        }

        void bindRecording(RecordLab.Recording recording) {
            mRecording = recording;
            //populate child views
            TextView soundTitle = itemView.findViewById(R.id.sound_title);
            TextView soundDuration = itemView.findViewById(R.id.sound_duration);
            TextView soundDate = itemView.findViewById(R.id.sound_date);
            int duration = 0;
            MediaPlayer mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(recording.getFilePath());
                mediaPlayer.prepare();
                duration = mediaPlayer.getDuration();
            } catch(IOException ioe) {
                ioe.printStackTrace();
            } finally {
                mediaPlayer.release();
            }

            soundTitle.setText(recording.getName());
            soundDate.setText(new SimpleDateFormat("yyyy-MM-dd",
                    Locale.UK).format(new Date(recording.getLastModified())));
            String[] timeComps = MyUtils.formatTimeComponents(MyUtils.getTimeComponents(duration));
            soundDuration.setText(mContext.getString(R.string.timer_placeholder,
                    timeComps[1],
                    timeComps[2]));
        }
    }

}

