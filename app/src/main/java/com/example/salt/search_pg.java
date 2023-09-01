package com.example.salt;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
    }
}
