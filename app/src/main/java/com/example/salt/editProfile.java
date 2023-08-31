package com.example.salt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class editProfile extends AppCompatActivity {

    private EditText chngUser, chngPhone, chngAge,oldPass, newPass, newCPass;
    private Button save_chg_btn;
    LinearLayout coordinator;
    DatabaseReference ref;
    FirebaseUser currentUser;
    ValueEventListener valueEventListener;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        chngPhone = findViewById(R.id.chngPhone);
        chngUser = findViewById(R.id.chngUser);
        chngAge = findViewById(R.id.chngAge);
        save_chg_btn = findViewById(R.id.save_chg_btn);
        oldPass = findViewById(R.id.oldPass);
        newPass = findViewById(R.id.newPass);
        newCPass = findViewById(R.id.newCPass);
        coordinator = findViewById(R.id.coordinator);

        ref = FirebaseDatabase.getInstance().getReference().child("user");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        valueEventListener = ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot das: snapshot.getChildren()){
                    Users user =das.getValue(Users.class);
                    if(user.getEmail().equals(currentUser.getEmail())){
                        uid = das.getKey();
                        System.out.println(uid);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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

        save_chg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!chngUser.getText().toString().equals("")){
                    String[] str = chngUser.getText().toString().trim().split("\\s+");
                    ref.child(uid).child("firstname").setValue(str[0]);
                    ref.child(uid).child("lastname").setValue(str[1]);
                }
                if(!chngPhone.getText().toString().equals("")){
                    String str = chngPhone.getText().toString().trim();
                    ref.child(uid).child("phone").setValue(str);
                }
//
                if(!chngAge.getText().toString().equals("")){
                    String str = chngAge.getText().toString().trim();
                    ref.child(uid).child("age").setValue(str);
                }
                
                if(!oldPass.getText().toString().equals("")){
                    if(!newPass.getText().toString().equals("")){
                        if(!newCPass.getText().toString().equals("")){
                            if (newPass.getText().toString().equals(newCPass.getText().toString())){
                                String email = currentUser.getEmail();
                                AuthCredential credential = EmailAuthProvider.getCredential(email,oldPass.getText().toString());
                                currentUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            currentUser.updatePassword(newPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(!task.isSuccessful()){
                                                        Snackbar snackbar_fail = Snackbar
                                                                .make(coordinator, "Something went wrong. Please try again later", Snackbar.LENGTH_LONG);
                                                        snackbar_fail.show();
                                                    }else {
                                                        Snackbar snackbar_su = Snackbar
                                                                .make(coordinator, "Password Successfully Modified", Snackbar.LENGTH_LONG);
                                                        snackbar_su.show();
                                                    }
                                                }
                                            });
                                        } else {
                                            Snackbar snackbar_su = Snackbar
                                                    .make(coordinator, "Authentication Failed", Snackbar.LENGTH_LONG);
                                            snackbar_su.show();
                                        }
                                    }
                                });
                            } else Toast.makeText(editProfile.this, "New Password and Confirm Password doesnt match", Toast.LENGTH_SHORT).show();
                        } else Toast.makeText(editProfile.this, "Confirm your new Password!", Toast.LENGTH_SHORT).show();
                    } else Toast.makeText(editProfile.this, "Fill in new password!", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(editProfile.this, "All Changes Saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}