package com.example.salt;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class viewPagerMessengerAdaptor extends FragmentPagerAdapter {

    public viewPagerMessengerAdaptor(@NonNull FragmentManager fm) {

        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return new songsUploadFrag();
        } else if(position == 1){
            return  new podcastUploadFrag();
        } else {
            return new localSongsFrag();
        }

   //     return null;
    }

    @Override
    public int getCount() {

        return 3; //number of tabs.
    }

    @Override
    public CharSequence getPageTitle(int position) {
//        return super.getPageTitle(position);
        if(position == 0){
            return "Songs";
        }else if(position == 1){
            return "Podcasts";
        } else {
            return "Local Songs";
        }
    }
}
