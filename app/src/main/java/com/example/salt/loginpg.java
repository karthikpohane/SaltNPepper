package com.example.salt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginpg extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private FirebaseAuth auth;
    int flag = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpg);

        //video
        VideoView videoview = (VideoView) findViewById(R.id.video_one);
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.video_one);
        videoview.setVideoURI(uri);
        videoview.start();
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                videoview.start(); //need to make transition seamless.
            }
        });

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        auth = FirebaseAuth.getInstance();

        // Checking if the user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null && flag !=0) {
//            Log.d(TAG, "Currently Signed in: " + currentUser.getEmail());
            Toast.makeText(loginpg.this, "Currently Logged in: " + currentUser.getEmail(), Toast.LENGTH_LONG).show();
            startActivity(new Intent(loginpg.this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
            flag = 0;
        }

        Button reg_btn = findViewById(R.id.reg_btn);
        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(loginpg.this, Registerpg.class);
                startActivity(intent);
            }
        });
        Button login_btn = findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailid = email.getText().toString();
                String pass = password.getText().toString();
                if(emailid.isEmpty() || pass.isEmpty())
                    Toast.makeText(loginpg.this, "Fill the Details!", Toast.LENGTH_SHORT).show();
                else
                    loginUser(emailid, pass);
            }
        });
    }

    private void loginUser(String emailid, String pass) {
        auth.signInWithEmailAndPassword(emailid, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(loginpg.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(loginpg.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        auth.signInWithEmailAndPassword(emailid, pass).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(loginpg.this, "Invalid Login Credentials!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}