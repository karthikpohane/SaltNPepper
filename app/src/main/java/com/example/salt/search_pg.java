package com.example.salt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class search_pg extends AppCompatActivity {

    private AutoCompleteTextView searchAutoCompleteTextView;
    private String[] dummyData = {
            "Apple", "Banana", "Cherry", "Date", "Fig", "Grape", "Kiwi",
            "Lemon", "Mango", "Orange", "Pineapple", "Strawberry"
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
    }
    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
