package com.example.salt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.example.salt.Adapter.JcSongsAdapter;
import com.example.salt.Model.GetSongs;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class homeFrag extends Fragment {
    private TextView greetingTextView,nameTextView;
    FirebaseUser currentUser;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    DatabaseReference ref,databaseReference;
    ValueEventListener valueEventListener;
    ArrayList<JcAudio> jcAudios = new ArrayList<>();
    private int currentIndex;
    JcPlayerView jcPlayerView;
    Boolean checkin = false;
    List<GetSongs> mupload;
    JcSongsAdapter adapter;

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
        TextView greetingTextView = view.findViewById(R.id.greetingTextView);
        nameTextView = view.findViewById(R.id.nameTextView);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("user");
        databaseReference = FirebaseDatabase.getInstance().getReference("songs");

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

        if (currentUser != null) {
            valueEventListener = ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot das : snapshot.getChildren()) {
                        Users user = das.getValue(Users.class);
                        if (user.getEmail().equals(currentUser.getEmail())) {
                            nameTextView.setText(user.getFirstname());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        return view;
    }
}