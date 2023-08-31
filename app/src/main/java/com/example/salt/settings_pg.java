package com.example.salt;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;

public class settings_pg extends AppCompatActivity {
    SwitchCompat switchCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_pg);
        //retrieving the previous states of toggle........

        //Night Mode Switch..
        switchCompat = findViewById(R.id.nightModeToggle);
        switchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(switchCompat.isChecked()){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    Toast.makeText(settings_pg.this, "Night Mode Onn!", Toast.LENGTH_SHORT).show();

                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    Toast.makeText(settings_pg.this, "Night Mode Off!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //For Private Account...
        switchCompat = findViewById(R.id.privateModeToggle);
        switchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(switchCompat.isChecked()){
                    Toast.makeText(settings_pg.this, "Your Account is Private", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(settings_pg.this, "Your Account is Public", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //For Security Settings..
        ImageView settings_btn = findViewById(R.id.settings_btn);
        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                startActivity(intent);
            }
        });

        //For Language settings..
        ImageView lang_btn = findViewById(R.id.lang_btn);
        lang_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(android.provider.Settings.ACTION_LOCALE_SETTINGS);
                startActivity(i);
            }
        });

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
        //Logout Button..........
        TextView logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(settings_pg.this);

                // Set the message show for the Alert time
                builder.setMessage("Do you want to exit ?");

                // Set Alert Title
                builder.setTitle("Alert !");

                // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
                builder.setCancelable(false);

                // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
                builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                    // When the user click yes button then app will close
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(settings_pg.this, "Logged Out!!", Toast.LENGTH_SHORT).show();
                    finish();
                    Intent intent = new Intent(getApplicationContext(), loginpg.class);
                    startActivity(intent);
                });
                builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                    // If user click no then dialog box is canceled.
                    dialog.cancel();
                });
                AlertDialog alertDialog = builder.create();
                // Show the Alert Dialog box
                alertDialog.show();

            }
        });
        //Edit Profile button......................
        Button editprofile_btn = findViewById(R.id.editprofile_btn);
        editprofile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), editProfile.class);
                startActivity(intent);
                finish();
            }
        });
        //Manage Account Button................
        Button manageacc_btn = findViewById(R.id.manageacc_btn);
        manageacc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), manageAccount.class);
                startActivity(intent);
                finish();
            }
        });
        //About Us page................
        ImageView aboutUs_btn = findViewById(R.id.aboutUs_btn);
        aboutUs_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(getApplicationContext(), aboutUs_pg.class);
                startActivity(intent);
                finish();
            }
        });

        //FAQs Button..........
        ImageView faq_btn = findViewById(R.id.faq_btn);
        faq_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), faq_pg.class);
                startActivity(intent);
                finish();
            }
        });

        //Write Us Button..............
        ImageView writeUs_btn = findViewById(R.id.writeUs_btn);
        writeUs_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mailIntent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:?subject=" + "I Want To Connect"+ "&to=" + "saltandpepper@gmail.com");
                mailIntent.setData(data);
                startActivity(Intent.createChooser(mailIntent, "Send mail..."));
            }
        });
    }
}