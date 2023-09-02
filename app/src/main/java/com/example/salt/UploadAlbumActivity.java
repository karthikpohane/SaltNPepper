package com.example.salt;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.salt.Model.Constants;
import com.example.salt.Model.Upload;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UploadAlbumActivity extends AppCompatActivity{

    private Button btnAlbumChoose, btnAlbumUpload;
    private EditText txtAlbumName;
    private ImageView albumImage;
    private Spinner spinner;
    String songsCategory;
    private static final int PICK_IMAGE_REQUEST = 234;
    private Uri filePath;

    FirebaseAuth auth;
    StorageReference storageReference;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_album);

        //Back Button.
        ImageView back_btn;
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), bolt_pg.class);
                startActivity(intent);
                finish();

            }
        });

        initViews();
        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);
        btnAlbumChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChoose();
            }
        });
        btnAlbumUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });

        List<String> categories = new ArrayList<>();

        categories.add("Love Songs");
        categories.add("Sad Songs");
        categories.add("Party Songs");
        categories.add("Birthday Songs");
        categories.add("God Songs");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                songsCategory = parent.getItemAtPosition(position).toString();
                Toast.makeText(UploadAlbumActivity.this, "Selected: "+songsCategory, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initViews() {
        btnAlbumUpload = findViewById(R.id.btnAlbumUpload);
        btnAlbumChoose = findViewById(R.id.btnAlbumChoose);
        txtAlbumName = findViewById(R.id.txtAlbumName);
        albumImage = findViewById(R.id.albumImage);
        spinner = findViewById(R.id.spinner);
    }

    private void uploadFile() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if(filePath !=null){
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("uploading...");
            progressDialog.show();
            final StorageReference sRef = storageReference.child(Constants.STORAGE_PATH_UPLOADS +
                    System.currentTimeMillis() + "."+getFileExtension(filePath));
            sRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            Upload upload = new Upload(txtAlbumName.getText().toString().trim(),
                                    url,songsCategory,currentUser.getEmail());
                            String uploadId = mDatabase.push().getKey();
                            assert uploadId != null;
                            mDatabase.child(uploadId).setValue(upload);
                            progressDialog.dismiss();
                            Toast.makeText(UploadAlbumActivity.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), likedSongs_pg.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(UploadAlbumActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                    progressDialog.setMessage("uploaded" + ((int)progress)+"%.....");
                }
            });
        }
    }

    private void showFileChoose() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            System.out.println("hi");
            System.out.println(data.getData());
            filePath = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                albumImage.setImageBitmap(bitmap);
                Toast.makeText(this, "Hi", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(this, "Hey", Toast.LENGTH_SHORT).show();
                throw new RuntimeException(e);
            }
        }
    }

    public String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getMimeTypeFromExtension(cr.getType(uri));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), bolt_pg.class);
        startActivity(intent);
        finish();
    }
}