package com.example.salt.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class GetSongs implements Parcelable {
    private String songCategory, songTitle, artist, album_art, songDuration, songLink, mKey;

    public GetSongs(String songCategory, String songTitle, String artist, String album_art, String songDuration, String songLink) {
        if(songTitle.trim().equals("")){
            songTitle = "No Title";
        }
        this.songCategory = songCategory;
        this.songTitle = songTitle;
        this.artist = artist;
        this.album_art = album_art;
        this.songDuration = songDuration;
        this.songLink = songLink;
    }

    public GetSongs() {
    }

    protected GetSongs(Parcel in) {
        songCategory = in.readString();
        songTitle = in.readString();
        artist = in.readString();
        album_art = in.readString();
        songDuration = in.readString();
        songLink = in.readString();
        mKey = in.readString();
    }

    public static final Creator<GetSongs> CREATOR = new Creator<GetSongs>() {
        @Override
        public GetSongs createFromParcel(Parcel in) {
            return new GetSongs(in);
        }

        @Override
        public GetSongs[] newArray(int size) {
            return new GetSongs[size];
        }
    };

    public String getSongCategory() {
        return songCategory;
    }

    public void setSongCategory(String songsCategory) {
        this.songCategory = songsCategory;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songsTitle) {
        this.songTitle = songsTitle;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum_art() {
        return album_art;
    }

    public void setAlbum_art(String album_art) {
        this.album_art = album_art;
    }

    public String getSongDuration() {
        return songDuration;
    }

    public void setSongDuration(String songDuration) {
        this.songDuration = songDuration;
    }

    public String getSongLink() {
        return songLink;
    }

    public void setSongLink(String songLink) {
        this.songLink = songLink;
    }

    public String getmKey() {
        return mKey;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(songCategory);
        dest.writeString(songTitle);
        dest.writeString(artist);
        dest.writeString(album_art);
        dest.writeString(songDuration);
        dest.writeString(songLink);
        dest.writeString(mKey);
    }
}
