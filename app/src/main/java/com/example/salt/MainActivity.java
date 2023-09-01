package com.example.salt;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.Manifest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bnView;
    private int selectedTab = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final LinearLayout homeLayout = findViewById(R.id.homeLayout);
        final LinearLayout searchLayout = findViewById(R.id.searchLayout);
        final LinearLayout boltLayout = findViewById(R.id.boltLayout);
        final LinearLayout favLayout = findViewById(R.id.favLayout);
        final LinearLayout settingLayout = findViewById(R.id.settingLayout);

        final ImageView homeImg = findViewById(R.id.homeImg);
        final ImageView searchImg = findViewById(R.id.searchImg);
        final ImageView boltImg = findViewById(R.id.boltImg);
        final ImageView favImg = findViewById(R.id.favImg);
        final ImageView settingImg = findViewById(R.id.settingImg);

        final TextView homeTxt = findViewById(R.id.homeTxt);
        final TextView searchTxt = findViewById(R.id.searchTxt);
        final TextView boltTXt = findViewById(R.id.boltTxt);
        final TextView favTxt = findViewById(R.id.favTxt);
        final TextView settingTxt = findViewById(R.id.settingTxt);

        //setting home as default.
        loadFragment(new homeFrag(), 0);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO},0);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);


        homeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedTab != 1) {

                    //Loading Fragments
//                    loadFragment(new homeFrag(),0);

                    //Text
                    searchTxt.setVisibility(View.GONE);
                    boltTXt.setVisibility(View.GONE);
                    favTxt.setVisibility(View.GONE);
                    settingTxt.setVisibility(View.GONE);

                    //Images
//                    searchImg.setImageResource(R.drawable.outline_search_24);
//                    favImg.setImageResource(R.drawable.outline_music_note_24);
//                    settingImg.setImageResource(R.drawable.outline_person_24);

                    //Layouts
                    searchLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    boltLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    favLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    settingLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    //Setting all the attributes of Home Fragment
                    homeTxt.setVisibility(View.VISIBLE);
                    homeImg.setImageResource(R.drawable.outline_home_selected_24);
                    homeLayout.setBackgroundResource(R.drawable.rounded_bg);

                    //animations
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    homeLayout.startAnimation(scaleAnimation);

                    selectedTab = 1;
                }

            }
        });

        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedTab != 2) {

                    //Loading Fragments
//                    loadFragment(new searchFrag(),0);
                    Intent intent = new Intent(getApplicationContext(), search_pg.class);
                    startActivity(intent);

                    //Text
                    homeTxt.setVisibility(View.GONE);
                    boltTXt.setVisibility(View.GONE);
                    favTxt.setVisibility(View.GONE);
                    settingTxt.setVisibility(View.GONE);

                    //Images
//                    homeImg.setImageResource(R.drawable.outline_home_24);
//                    favImg.setImageResource(R.drawable.outline_music_note_24);
//                    settingImg.setImageResource(R.drawable.outline_person_24);

                    //Layouts
                    homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    boltLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    favLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    settingLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    //Setting all the attributes of Home Fragment
                    searchTxt.setVisibility(View.VISIBLE);
                    searchImg.setImageResource(R.drawable.outline_search_selected_24);
                    searchLayout.setBackgroundResource(R.drawable.rounded_bg);

                    //animations
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    searchLayout.startAnimation(scaleAnimation);

                    selectedTab = 2;

                }
            }
        });

        boltLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedTab != 3) {

                    //Loading Fragments
//                    loadFragment(new homeFrag(),0);

                    //calling bolt_pg activity.
                    Intent intent = new Intent(getApplicationContext(), bolt_pg.class);
                    startActivity(intent);

                    //Text
                    homeTxt.setVisibility((View.GONE));
                    searchTxt.setVisibility(View.GONE);
                    favTxt.setVisibility(View.GONE);
                    settingTxt.setVisibility(View.GONE);

                    //Images
//                    searchImg.setImageResource(R.drawable.outline_search_24);
//                    favImg.setImageResource(R.drawable.outline_music_note_24);
//                    settingImg.setImageResource(R.drawable.outline_person_24);

                    //Layouts
                    homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    searchLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    favLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    settingLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    //Setting all the attributes of Home Fragment
                    boltTXt.setVisibility(View.VISIBLE);
                    boltImg.setImageResource(R.drawable.bolt_selected_24);
                    boltLayout.setBackgroundResource(R.drawable.rounded_bg);

                    //animations
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 2.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    boltLayout.startAnimation(scaleAnimation);

                    selectedTab = 3;
                }

            }
        });

        favLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedTab != 4) {

                    //Loading Fragments
//                    loadFragment(new likedFrag(),0);

                    //calling Intent..
                    Intent intent = new Intent(getApplicationContext(), likedSongs_pg.class);
                    startActivity(intent);

                    //Text
                    homeTxt.setVisibility(View.GONE);
                    searchTxt.setVisibility(View.GONE);
                    boltTXt.setVisibility(View.GONE);
                    settingTxt.setVisibility(View.GONE);

                    //Images
//                    homeImg.setImageResource(R.drawable.outline_home_24);
//                    searchImg.setImageResource(R.drawable.outline_search_24);
//                    settingImg.setImageResource(R.drawable.outline_person_24);

                    //Layouts
                    homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    searchLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    boltLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    settingLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    //Setting all the attributes of Home Fragment
                    favTxt.setVisibility(View.VISIBLE);
                    favImg.setImageResource(R.drawable.outline_music_note_selected_24);
                    favLayout.setBackgroundResource(R.drawable.rounded_like_bg);

                    //animations
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 3.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    searchLayout.startAnimation(scaleAnimation);

                    selectedTab = 4;
                }

            }
        });

        settingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedTab != 5) {

                    //Loading Fragments
//                    loadFragment(new profileFrag(),0);

                    //calling another activity.
                    Intent intent = new Intent(getApplicationContext(), settings_pg.class);
                    startActivity(intent);

                    //Text
                    homeTxt.setVisibility(View.GONE);
                    searchTxt.setVisibility(View.GONE);
                    boltTXt.setVisibility(View.GONE);
                    favTxt.setVisibility(View.GONE);

                    //Images
//                    homeImg.setImageResource(R.drawable.outline_home_24);
//                    searchImg.setImageResource(R.drawable.outline_search_24);
//                    favImg.setImageResource(R.drawable.outline_music_note_24);

                    //Layouts
                    homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    searchLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    boltLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    favLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    //Setting all the attributes of Home Fragment
                    settingTxt.setVisibility(View.VISIBLE);
                    settingImg.setImageResource(R.drawable.outline_person_selected_24);
                    settingLayout.setBackgroundResource(R.drawable.rounded_bg);

                    //animations
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 4.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    searchLayout.startAnimation(scaleAnimation);

                    selectedTab = 5;
                }

            }
        });
    }

//        bnView = findViewById(R.id.bnView);
//
//        bnView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//                int id = item.getItemId();
//                int flag = 1;
//                if (id == R.id.profile) {
//                    loadFragment(new profileFrag(), 0);
//
//                } else if (id == R.id.search) {
//
//                    loadFragment(new searchFrag(), 0);
//
//                } else if (id == R.id.store) {
//
//                    loadFragment(new storeFrag(), 0);
//
//                } else if(id == R.id.like){
//                    loadFragment(new likedFrag(), 0);
//
//                } else {
//                    //for Home fragment.
//                    loadFragment(new homeFrag(), 0);
//                }
//                return true;
//            }
//        });
//
//        bnView.setSelectedItemId(R.id.home);
//    }

    public void loadFragment(Fragment fragment, int flag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (flag != 0) {
            ft.setReorderingAllowed(true).add(R.id.fragmentContainer, fragment);
        } else {
            ft.setReorderingAllowed(true).replace(R.id.container, fragment);
        }
//        ft.setReorderingAllowed(true).replace(R.id.container, fragment);
        ft.commit();

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        // Set the message show for the Alert time
        builder.setMessage("Do you want to exit ?");

        // Set Alert Title
        builder.setTitle("Alert !");

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            // When the user click yes button then app will close
            finishAffinity();
        });
        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            // If user click no then dialog box is canceled.
            dialog.cancel();
        });
        AlertDialog alertDialog = builder.create();
        // Show the Alert Dialog box
        alertDialog.show();
    }
}