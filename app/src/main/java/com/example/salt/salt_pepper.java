package com.example.salt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;

public class salt_pepper extends AppCompatActivity {
    //Channel id and notification id.
    private static final String CHANNEL_ID = "Podcast Notification";
    private static final int NOTIFICATION_ID = 101;
    Notification notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salt_pepper);
        //Conversion of an image into a Bitmap.....
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.noti_large, null);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap largeIcon = bitmapDrawable.getBitmap();

        //Notification..........
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this)
                    .setLargeIcon(largeIcon)
                    .setSmallIcon(R.drawable.logothree)
                    .setContentText("You can create your own podcasts and publish them for FREE, and get Recognised")
                    .setSubText("Upload And Get Recognised")
                    .setChannelId(CHANNEL_ID)
                    .build();
            nm.createNotificationChannel(new NotificationChannel(CHANNEL_ID, "Podcast Channel", NotificationManager.IMPORTANCE_HIGH));
        } else {
            notification = new Notification.Builder(this)
                    .setLargeIcon(largeIcon)
                    .setSmallIcon(R.drawable.logothree)
                    .setContentText("You can create your own podcasts and publish them for FREE, and get Recognised")
                    .setSubText("Upload And Get Recognised")
                    .build();
        }
        nm.notify(NOTIFICATION_ID, notification);

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