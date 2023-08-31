package com.example.salt;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cleveroad.audiovisualization.BuildConfig;
import com.example.salt.dialog.DeleteRecordingDialog;
import com.example.salt.dialog.RenameRecordingDialog;

import java.io.File;
import java.util.List;

public class RecordingListFragment extends Fragment {
    public static final String PREF_SORT_BY = RecordLab.PREF_SORT_BY;
    public static final int PREF_SORT_BY_NAME = RecordLab.PREF_SORT_BY_NAME;
    public static final int PREF_SORT_BY_RECENT = RecordLab.PREF_SORT_BY_RECENT;

    private RecyclerView mRecyclerView;
    private RecordingListAdapter mListAdapter;
    private RecordLab mRecordLab;
    private RecordLab.Recording mSelectedRecording;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case DeleteRecordingDialog.DELETE_REQUEST_CODE: {
                if (resultCode == Activity.RESULT_OK) {
                    boolean isDeleted = mRecordLab.deleteRecording(mSelectedRecording.getName());
                    if (isDeleted) {
                        reloadList();
                        Toast.makeText(getContext(), "File Deleted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Problem Occurred While Deleting File", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
            case RenameRecordingDialog.RENAME_REQUEST_CODE: {
                if (resultCode == Activity.RESULT_OK) {
                    boolean isRenamed = mRecordLab.renameRecording(mSelectedRecording.getName(),
                            data.getStringExtra(RenameRecordingDialog.EXTRA_NEW_NAME));
                    if (isRenamed) {
                        reloadList();
                        Toast.makeText(getContext(), "File Renamed", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Problem Occurred While Renaming File", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sound_list, container, false);
        // reference the mRecyclerView and
        mRecyclerView = view.findViewById(R.id.recycler_view);
        //setup its layout manager (Compulsory)
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecordLab = new RecordLab(getContext());
        // set mRecyclerView adapter
        mListAdapter = new RecordingListAdapter(this, mRecordLab.getRecordings());
        mRecyclerView.setAdapter(mListAdapter);

        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadList();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return view;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.delete_recording){
            DeleteRecordingDialog dialog = DeleteRecordingDialog.newInstance(RecordingListFragment.this);
            dialog.show(getFragmentManager(), "Delete Recording");
            return true;
        } else if(item.getItemId() == R.id.share_recording){
            Toast.makeText(getContext(), "Share Recording", Toast.LENGTH_SHORT).show();
            shareRecording(mSelectedRecording.getName());
            return true;
        } else if (item.getItemId() == R.id.rename_recording) {
            RenameRecordingDialog dialog = RenameRecordingDialog.newInstance(RecordingListFragment.this, mSelectedRecording.getName());
            dialog.show(getFragmentManager(), "Rename Recording");
            return true;
        } else
            return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.recording_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.sort_by_name){
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            preferences.edit()
                    .putInt(PREF_SORT_BY, PREF_SORT_BY_NAME).apply();
            reloadList();
            return true;
        } else if (item.getItemId() == R.id.sort_by_recent){
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            preferences.edit()
                    .putInt(PREF_SORT_BY, PREF_SORT_BY_RECENT).apply();
            reloadList();
            return true;
        } else
            return super.onOptionsItemSelected(item);

    }

    // updates a current recording to highlighted recording
    public void setSelectedRecording(RecordLab.Recording recording) {
        mSelectedRecording = recording;
    }

    public void startOpenIntent(boolean withChooser) {
    }

    // creates an implicit action_send intent to share the mSelectedRecording file to other app/devices
    public void shareRecording(String fileName) {
        File recordDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
                Environment.DIRECTORY_PODCASTS);
        File recording = new File(recordDir, fileName);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType(Intent.normalizeMimeType("audio/" + "mp3"));
        shareIntent = Intent.createChooser(shareIntent, getContext().getString(R.string.share_via_label));
        Uri recordingUri = FileProvider.getUriForFile(getContext(),
                BuildConfig.APPLICATION_ID + ".fileprovider", recording);
        shareIntent.putExtra(MediaStore.EXTRA_OUTPUT, recordingUri);

        PackageManager packageManager = getActivity().getPackageManager();
        List<ResolveInfo> resolveInfos = packageManager.
                queryIntentActivities(shareIntent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo info: resolveInfos) {
            getActivity().grantUriPermission(info.activityInfo.packageName, recordingUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        startActivity(shareIntent);
    }

    // reloads the recyclerView list
    public void reloadList() {
        RecordLab recordLab = new RecordLab(getContext());
        mListAdapter.setRecordings(recordLab.getRecordings());
        mListAdapter.notifyDataSetChanged();
    }


}

