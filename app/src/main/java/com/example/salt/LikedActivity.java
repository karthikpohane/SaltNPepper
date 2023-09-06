package com.example.salt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.example.salt.Adapter.JcSongsAdapter;
import com.example.salt.Model.GetSongs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LikedActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    Boolean checkin = false;
    List<GetSongs> mupload;
    JcSongsAdapter adapter;
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;
    JcPlayerView jcPlayerView;
    ArrayList<JcAudio> jcAudios = new ArrayList<>();
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);

        //Back Button.
        ImageView back_btn;
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), bolt_pg.class);
                startActivity(intent);
                finish();

            }
        });

        intiViews();

        adapter = new JcSongsAdapter(getApplicationContext(), mupload, new JcSongsAdapter.RecyclerItemClickListner() {
            @Override
            public void onClickListener(GetSongs songs, int position) {

                changeSelectedSong(position);
                OneSongFragment fragment = new OneSongFragment(position);
                fragment.setSong(songs);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.songFrag,fragment);
                ft.commit();
//                Intent intent = new Intent(AllSongsActivity.this,OneSongActivity.class);
//                intent.putExtra("song", songs);
//                startActivity(intent);
//                jcPlayerView.playAudio(jcAudios.get(position));
//                jcPlayerView.setVisibility(View.VISIBLE);
//                jcPlayerView.createNotification();
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference("liked");
        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
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
                adapter.setSelectedPosition(0);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

                if(checkin){
                    jcPlayerView.initPlaylist(jcAudios,null);
                } else {
                    Toast.makeText(LikedActivity.this, "there is no songs!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void changeSelectedSong(int index){
        adapter.notifyItemChanged(adapter.getSelectedPosition());
        currentIndex = index;
        adapter.setSelectedPosition(currentIndex);
        adapter.notifyItemChanged(currentIndex);
    }

    private void intiViews() {
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressSong);
        jcPlayerView = findViewById(R.id.jcplayer);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        mupload = new ArrayList<>();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), bolt_pg.class);
        startActivity(intent);
        finish();
    }

    public ArrayList<JcAudio> getJcAudios() {
        return jcAudios;
    }
}