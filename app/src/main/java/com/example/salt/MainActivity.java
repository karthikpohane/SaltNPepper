package com.example.salt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.example.salt.Adapter.JcSongsAdapter;
import com.example.salt.Adapter.JcSongsAdapter2;
import com.example.salt.Model.GetSongs;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bnView;
    private int selectedTab = 1;

    private TextView greetingTextView,nameTextView;
    FirebaseUser currentUser;
    DatabaseReference ref,databaseReference;
    ValueEventListener valueEventListener,valueEventListener1,valueEventListener2;
    ArrayList<JcAudio> jcAudios = new ArrayList<>();
    private int currentIndex;
    RecyclerView recyclerView1,recyclerView2;
    Boolean checkin = false;
    List<GetSongs> mupload;
    JcSongsAdapter adapter,adapter1;
    JcSongsAdapter2 adapter2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Getting Greetings..........
//        greetingTextView = findViewById(R.id.greetingTextView);
        recyclerView1 = findViewById(R.id.recyclerView1);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        recyclerView1.setAdapter(adapter);
        recyclerView2 = findViewById(R.id.recyclerView2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        recyclerView2.setAdapter(adapter1);
        mupload = new ArrayList<>();

        // Get the current time
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        // Set the appropriate greeting based on the time of day
        String greeting;
        if (hourOfDay >= 0 && hourOfDay < 12) {
            greeting = "Good Morning!";
        } else if (hourOfDay >= 12 && hourOfDay < 18) {
            greeting = "Good Afternoon!";
        } else {
            greeting = "Good Evening!";
        }

//        greetingTextView.setText(greeting);

        //Name Display.......
        nameTextView = findViewById(R.id.nameTextView);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("user");
        databaseReference = FirebaseDatabase.getInstance().getReference("songs");
        if (currentUser != null) {
            valueEventListener = ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot das : snapshot.getChildren()) {
                        Users user = das.getValue(Users.class);
                        if (user.getEmail().equals(currentUser.getEmail())) {
                            nameTextView.setText(user.getEmail());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


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
//        loadFragment(new homeFrag(), 0);
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

        //----------- song display ----------------
        adapter2 = new JcSongsAdapter2(getApplicationContext(), mupload, new JcSongsAdapter2.RecyclerItemClickListner() {
            @Override
            public void onClickListener(GetSongs songs, int position) {
                changeSelectedSong(position);
                System.out.println("Hey");
                OneSongFragment fragment = new OneSongFragment(position);
                fragment.setSong(songs);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.songFrag,fragment);
                ft.commit();
            }
        });

        valueEventListener1 = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mupload.clear();
                for(DataSnapshot das: snapshot.getChildren()){
                    GetSongs getSongs = das.getValue(GetSongs.class);
                    getSongs.setmKey(das.getKey());
                    currentIndex = 0;
                    mupload.add(getSongs);
                    checkin = true;
                    jcAudios.add(JcAudio.createFromURL(getSongs.getSongTitle(),getSongs.getSongLink()));
                }
                adapter2.setSelectedPosition(0);
                recyclerView1.setAdapter(adapter2);
                adapter2.notifyDataSetChanged();

                if(checkin){
                } else {
                    Toast.makeText(MainActivity.this, "there is no songs!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter1 = new JcSongsAdapter(getApplicationContext(), mupload, new JcSongsAdapter.RecyclerItemClickListner() {
            @Override
            public void onClickListener(GetSongs songs, int position) {
                changeSelectedSong(position);
                OneSongFragment fragment = new OneSongFragment(position);
                fragment.setSong(songs);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.songFrag,fragment);
                ft.commit();
            }
        });

        valueEventListener2 = databaseReference.limitToLast(5).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mupload.clear();
                for(DataSnapshot das: snapshot.getChildren()){
                    GetSongs getSongs = das.getValue(GetSongs.class);
                    getSongs.setmKey(das.getKey());
                    currentIndex = 0;
                    mupload.add(getSongs);
                    checkin = true;
                    jcAudios.add(JcAudio.createFromURL(getSongs.getSongTitle(),getSongs.getSongLink()));
                }
                adapter1.setSelectedPosition(0);
                recyclerView2.setAdapter(adapter1);
                adapter1.notifyDataSetChanged();

                if(checkin){
                } else {
                    Toast.makeText(MainActivity.this, "there is no songs!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void changeSelectedSong(int index){
        adapter2.notifyItemChanged(adapter2.getSelectedPosition());
        currentIndex = index;
        adapter2.setSelectedPosition(currentIndex);
        adapter2.notifyItemChanged(currentIndex);
    }
    public ArrayList<JcAudio> getJcAudios() {
        return jcAudios;
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

//    public void loadFragment(Fragment fragment, int flag) {
//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        if (flag != 0) {
//            ft.setReorderingAllowed(true).add(R.id.fragmentContainer, fragment);
//        } else {
//            ft.setReorderingAllowed(true).replace(R.id.container, fragment);
//        }
////        ft.setReorderingAllowed(true).replace(R.id.container, fragment);
//        ft.commit();
//
//    }

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