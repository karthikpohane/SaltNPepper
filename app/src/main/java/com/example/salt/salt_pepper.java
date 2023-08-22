package com.example.salt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class salt_pepper extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salt_pepper);

        //Defining Intent.
        Intent iHome = new Intent(salt_pepper.this, loginpg.class);

        //Delay Code if you ever feel like using it just remove the intent code and place the code you want to be executed after delay.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(iHome); //Calling Intent.
                finish();
            }
        }, 2000);

    }
}