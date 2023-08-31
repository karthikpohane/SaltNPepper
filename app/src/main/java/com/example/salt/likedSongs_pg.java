package com.example.salt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class likedSongs_pg extends AppCompatActivity {
    private Animation rotateOpen;
    private Animation rotateClose;
    private Animation fromBottom;
    private Animation toBottom;
    private boolean clicked = false;

    FloatingActionButton heart_btn, upload_btn, manageacc_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_songs_pg);
        //back button
        ImageView back_btn;
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        rotateOpen = AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim);

        heart_btn = findViewById(R.id.heart_btn);
        heart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddButtonClicked();
            }
        });
        upload_btn = findViewById(R.id.upload_btn);
        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Upload Page!!", Toast.LENGTH_SHORT).show();
            }
        });
        manageacc_btn = findViewById(R.id.manageacc_btn);
        manageacc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Manage Account Page", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onAddButtonClicked() {
        setVisibility(clicked);
        setAnimation(clicked);
        clicked = !clicked;
    }

    private void setVisibility(boolean clicked) {
        if (!clicked) {
            upload_btn.setVisibility(View.VISIBLE);
            manageacc_btn.setVisibility(View.VISIBLE);
        } else {
            upload_btn.setVisibility(View.INVISIBLE);
            manageacc_btn.setVisibility(View.INVISIBLE);
        }
    }

    private void setAnimation(boolean clicked) {
        if (!clicked) {
            upload_btn.startAnimation(fromBottom);
            manageacc_btn.startAnimation(fromBottom);
            heart_btn.startAnimation(rotateOpen);
        } else {
            upload_btn.startAnimation(toBottom);
            manageacc_btn.startAnimation(toBottom);
            heart_btn.startAnimation(rotateClose);
        }
    }
    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}