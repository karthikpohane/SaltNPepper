package com.example.salt;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class search_pg extends AppCompatActivity {

    ConstraintLayout card1, card2, card3, card4, card5;

    private AutoCompleteTextView searchAutoCompleteTextView;
    private String[] dummyData = {
            "Kisi ki Muskurahaton Pe Hoon Nisaar",
            "Zara Haule Haule Chalo More Sajna",
            "Lag Jaa Gale",
            "With you",
            "9:45",
            "Heeriye",
            "Capital Letters",
            "Just not Into you",
            "Crown",
            "Sofia",
            "Blinding Lights",
            "If i have you",
            "Badass Babua"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_pg);

        searchAutoCompleteTextView = findViewById(R.id.searchAutoCompleteTextView);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, dummyData);
        searchAutoCompleteTextView.setAdapter(adapter);
        searchAutoCompleteTextView.setThreshold(1); // Show suggestions after typing 1 character

        searchAutoCompleteTextView.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                String selectedText = searchAutoCompleteTextView.getText().toString();
                if (isItemInList(selectedText)) {
                    Toast.makeText(this, "You selected: " + selectedText, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Nothing matched", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            return false;
        });

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

        //Cards Setting........
        card1 = findViewById(R.id.card1);
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(search_pg.this, "Top 5 trending songs - Error: No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });

        card2 = findViewById(R.id.card2);
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(search_pg.this, "Top 5 Bollywood songs - Error: No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });

        card3 = findViewById(R.id.card3);
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(search_pg.this, "Top 5 Hollywood songs - Error: No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });

        card4 = findViewById(R.id.card4);
        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(search_pg.this, "Songs You may Like - Error: No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });

        card5 = findViewById(R.id.card5);
        card5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(search_pg.this, "Popular Uploads - Error: No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private boolean isItemInList(String item) {
        for (String data : dummyData) {
            if (data.equalsIgnoreCase(item)) {
                return true;
            }
        }
        return false;
    }
}

