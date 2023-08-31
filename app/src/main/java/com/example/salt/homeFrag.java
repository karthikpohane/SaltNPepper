package com.example.salt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class homeFrag extends Fragment {
    private TextView greetingTextView;

    public homeFrag() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        TextView txtFrag = view.findViewById(R.id.txtFrag);
        TextView greetingTextView = view.findViewById(R.id.greetingTextView);

        // Get the current time
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        // Set the appropriate greeting based on the time of day
        String greeting;
        if (hourOfDay >= 0 && hourOfDay < 12) {
            greeting = "Good Morning!";
        } else if (hourOfDay >= 12 && hourOfDay < 18) {
            greeting = "Good Afternoon!";
        } else {
            greeting = "Good  Evening!";
        }

        greetingTextView.setText(greeting);
        return view;
    }
}