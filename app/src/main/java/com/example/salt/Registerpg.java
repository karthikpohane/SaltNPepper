package com.example.salt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;

public class Registerpg extends AppCompatActivity {

    private ImageView logo;
    private Button regdone_btn;
    private EditText firstname;
    private EditText lastname;

    private EditText editTextTextPassword;
    private EditText editTextTextEmailAddress;
    private EditText editTextPhone;
    private EditText age;
    private RadioGroup radioGroup;

    private FirebaseAuth auth;

    StorageReference storageReference;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerpg);

        firstname=findViewById(R.id.firstname);
        lastname=findViewById(R.id.lastname);
        editTextTextEmailAddress=findViewById(R.id.editTextTextEmailAddress);
        editTextTextPassword = findViewById(R.id.editTextTextPassword);
        editTextPhone=findViewById(R.id.editTextPhone);
        age = findViewById(R.id.age);
        radioGroup =(RadioGroup) findViewById(R.id.radioGroup);

        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference("user");

        regdone_btn = findViewById(R.id.regdone_btn);
        regdone_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String first = (String)firstname.getText().toString();
                String last = (String)lastname.getText().toString();
                String email = (String)editTextTextEmailAddress.getText().toString();
                String pass = (String)editTextTextPassword.getText().toString();
                String mob = (String)editTextPhone.getText().toString();
                String ag = (String)age.getText().toString();
                // Toast.makeText(Registerpg.this, "1 Please Fill in the details", Toast.LENGTH_SHORT).show();
                //

                radioGroup.clearCheck();
                int sx=radioGroup.getCheckedRadioButtonId();
                if (first.isEmpty() || last.isEmpty() || email.isEmpty() || pass.isEmpty() || mob.isEmpty() || ag.isEmpty() || sx>=0&&sx<=3) {
                    Toast.makeText(Registerpg.this, "Please Fill in the details", Toast.LENGTH_SHORT).show();
                } else if(pass.length()<6) {
                    Toast.makeText(Registerpg.this, "Too Small Password", Toast.LENGTH_SHORT).show();
                }else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(Registerpg.this);
                    builder1.setMessage("Are You Sure you want to Register?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    registerUser(email, pass);
                                    Intent intent = new Intent(getApplicationContext(), loginpg.class);
//                                    intent.putExtra("first", first);
//                                    intent.putExtra("last", last);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(Registerpg.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    for (TextView editText : Arrays.asList(firstname, lastname, editTextTextEmailAddress, editTextPhone, editTextTextPassword, age)) {
                                        editText.setText("");
                                    }
//                              Action for 'NO' Button

                                    Toast.makeText(getApplicationContext(), "Registration failed!",
                                            Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            }
        });
        logo = findViewById(R.id.logo);

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), loginpg.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void registerUser(String email, String pass) {
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(Registerpg.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
//                    Toast.makeText(Registerpg.this, "Registration Completed!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Registerpg.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Users users = new Users(firstname.getText().toString().trim(), lastname.getText().toString().trim(), editTextTextEmailAddress.getText().toString().trim(), editTextPhone.getText().toString().trim(), age.getText().toString().trim());
                String uploadid = mDatabase.push().getKey();
                assert uploadid != null;
                mDatabase.child(uploadid).setValue(users);
            }
        });

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Registerpg.this);

        // Set the message show for the Alert time
        builder.setMessage("Do you want to exit ?");

        // Set Alert Title
        builder.setTitle("Alert !");

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            // When the user click yes button then app will close
            finish();
        });
        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            // If user click no then dialog box is canceled.
            dialog.cancel();
        });
        AlertDialog alertDialog = builder.create();
        // Show the Alert Dialog box
        alertDialog.show();
    }
}