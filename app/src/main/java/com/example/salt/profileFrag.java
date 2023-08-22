package com.example.salt;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profileFrag extends Fragment {

    private TextView txt_first;
    private TextView txt_last;
    private TextView txt_email;
    private TextView txt_phn;
    private TextView txt_age;
    private DatabaseReference databaseReference;
    ValueEventListener valueEventListener;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        txt_first = view.findViewById(R.id.txt_first);
        txt_last = view.findViewById(R.id.txt_last);
        txt_email = view.findViewById(R.id.txt_email);
        txt_phn = view.findViewById(R.id.txt_phn);
        txt_age = view.findViewById(R.id.txt_age);
        databaseReference = FirebaseDatabase.getInstance().getReference("user");

        Button logoutbtn = view.findViewById(R.id.logoutbtn);
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getContext(), "Logged Out!!", Toast.LENGTH_SHORT).show();
                Intent intent;
                intent = new Intent(getContext(), loginpg.class);
                startActivity(intent);
            }
        });

        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot das: snapshot.getChildren()) {

//                    if (FirebaseAuth.getInstance().getCurrentUser() == )
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}