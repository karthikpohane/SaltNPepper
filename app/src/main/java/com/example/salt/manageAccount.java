package com.example.salt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;

public class manageAccount extends AppCompatActivity {
    TabLayout tabView;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);

        tabView = findViewById(R.id.tabView);
        viewPager = findViewById(R.id.viewPager);

        viewPagerMessengerAdaptor adaptor = new viewPagerMessengerAdaptor(getSupportFragmentManager());
        viewPager.setAdapter(adaptor);
        tabView.setupWithViewPager(viewPager);

        ImageView back_btn;
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(getApplicationContext(), settings_pg.class);
                startActivity(intent);
            }
        });
    }
}