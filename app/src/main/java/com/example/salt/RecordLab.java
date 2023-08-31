package com.example.salt;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RecordLab {
    public static final String PREF_SORT_BY = "sort by";
    public static final int PREF_SORT_BY_NAME = 0;
    public static final int PREF_SORT_BY_RECENT = 1;

    private Context mContext;
    private ArrayList<Recording> mRecordings;
    private File mRecordDirectory;

    public RecordLab(Context context) {
        mContext = context;
        mRecordDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
                Environment.DIRECTORY_PODCASTS);
    }

    // attempts to delete a recording from device and returns if deletion was successful or not
    public boolean deleteRecording(String fileName) {
        File recordDir = new File (Environment.getExternalStorageDirectory().getAbsolutePath(),
                Environment.DIRECTORY_PODCASTS);
        File record = new File(recordDir, fileName);
        return record.delete();
    }

    // renames a recording to a user provided name and returns if rename was successful or not
    public boolean renameRecording(String fileName, String newFileName) {
        File record = new File(mRecordDirectory, fileName);
        File renamedFile = new File(record.getParent(), newFileName + ".mp3");
        return (record.renameTo(renamedFile));
    }

    // this retrieves the list of recordings from the device
    public List<Recording> getRecordings() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        int sortOrder = preferences.getInt(PREF_SORT_BY, PREF_SORT_BY_NAME);
        ArrayList<Recording> recordings = new ArrayList<>();
        File dir = new File (Environment.getExternalStorageDirectory().getAbsolutePath(),
                Environment.DIRECTORY_PODCASTS);
        File[] fileList = dir.listFiles();
        for (int i = fileList.length - 1; i >= 0; i-- ) {
            File f = fileList[i];
            if (!f.isDirectory()) {
                Recording recording = new Recording(f);
                recordings.add(recording);
            }
        }
        if (sortOrder == PREF_SORT_BY_NAME) Collections.sort(recordings, new NameComparator());
        else Collections.sort(recordings, new RecentComparator());
        return recordings;
    }
    // Comparator Class for sorting recordings according to their names
    private static class NameComparator implements Comparator<Recording> {
        @Override
        public int compare(Recording obj1, Recording obj2) {
            return obj1.getName().compareToIgnoreCase(obj2.getName());
        }
    }

    // Comparator Class for sorting recordings according to their dates
    private static class RecentComparator implements Comparator<Recording> {
        @Override
        public int compare(Recording obj1, Recording obj2) {
            if (obj1.getLastModified() > obj2.getLastModified()) return -1;
            else if (obj1.getLastModified() < obj2.getLastModified()) return 1;
            else return 0;
        }
    }

    public static class Recording {
        private String mName;
        private String mFilePath;
        private long mLastModified;

        public Recording(File file) {
            mFilePath = file.getAbsolutePath();
            String[] pathComponents = mFilePath.split(File.separator);
            String name = pathComponents[pathComponents.length - 1];
            String[] nameComponents = name.split(".");
            if (nameComponents.length > 0) {
                for (String n: nameComponents) System.out.println(n + "\n");
            }
            mName = name;
            mLastModified = file.lastModified();
        }

        public String getName() {
            return mName;
        }

        public long getLastModified() {
            return mLastModified;
        }

        public String getFilePath() { return mFilePath; }
    }
}

