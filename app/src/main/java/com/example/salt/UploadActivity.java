package com.example.salt;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.salt.Model.Constants;
import com.example.salt.Model.UploadSong;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UploadActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView txtSongFileSelected;
    private ProgressBar progressBar;
    private Uri audioUri,filePath;
    private StorageReference mStorageRef, storageReference;
    private StorageTask mUploadsTask;
    private DatabaseReference referenceSongs;
    String songsCategory;
    MediaMetadataRetriever metadataRetriever;
    byte [] art;
    private String title1, artist1, album_art1 = "", duration1;
    private EditText title, artist, album, dataa;
    private ImageView album_art;

    private Button open,btnSubmit,btnUploadImg;
    FirebaseAuth mAuth;

    private static final int PICK_IMAGE_REQUEST = 234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

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
        mAuth = FirebaseAuth.getInstance();

        metadataRetriever = new MediaMetadataRetriever();
        referenceSongs = FirebaseDatabase.getInstance().getReference().child("songs");
        mStorageRef = FirebaseStorage.getInstance().getReference().child("songs");
        storageReference = FirebaseStorage.getInstance().getReference();

        Spinner spinner = findViewById(R.id.spinner_cat);
        spinner.setOnItemSelectedListener(this);

        List<String> categories  = new ArrayList<>();

        categories.add("Love Songs");
        categories.add("Sad Songs");
        categories.add("Party Songs");
        categories.add("Birthday Songs");
        categories.add("Religious Songs");

        ArrayAdapter <String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAudioFiles(v);
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFileTofirebase(v);
            }
        });
        btnUploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChoose();
            }
        });
    }

    private void initViews() {
        txtSongFileSelected = findViewById(R.id.txtSongFileSelected);
        progressBar = findViewById(R.id.progress);
        title = findViewById(R.id.editTitle);
        artist = findViewById(R.id.editArtist);
        album = findViewById(R.id.editAlbum);
        dataa= findViewById(R.id.dataa);
        album_art = findViewById(R.id.albumImg);
        open = findViewById(R.id.open);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnUploadImg = findViewById(R.id.btnUploadImg);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        songsCategory = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void openAudioFiles(View v){
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("audio/*");
        startActivityForResult(i, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 101 && resultCode == RESULT_OK && data.getData() != null){
            audioUri = data.getData();
            String fileName = getFileName(audioUri);
            txtSongFileSelected.setText(fileName);
            metadataRetriever.setDataSource(this, audioUri);
            duration1 = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            filePath = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                album_art.setImageBitmap(bitmap);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @SuppressLint("Range") //This is an annotation used to suppress lint warnings. In this case, it is suppressing warnings related to "Range," which usually indicates the use of the new indexing syntax available from Android API level 21.
    //private method that takes a Uri as input and returns a String, which is the file name associated with that Uri.
    private String getFileName(Uri uri){
        String result = null;
        if(uri.getScheme().equals("content")){
            Cursor cursor = getContentResolver().query(uri, null, null, null,null);
            try{
                if(cursor != null && cursor.moveToFirst()){
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    //Retrieves the value of the column named DISPLAY_NAME from the Cursor. The OpenableColumns.DISPLAY_NAME constant represents the display name of the file associated with the content Uri.
                }
            }
            finally{
                assert cursor != null;
                cursor.close();
            }
        }

        if (result == null){ // If the file name hasn't been found from the content Uri
            result = uri.getPath(); //Gets the file path from the Uri.
            int cut = result.lastIndexOf('/'); //Finds the last occurrence of the slash ('/') character in the file path.
            if(cut != -1){
                result = result.substring(cut+1);
                //Extracts the substring from the file path, starting after the last slash character. This effectively gives the file name from the path.
            }
        }
        return result;
    }

    public void uploadFileTofirebase(View v){
        if(txtSongFileSelected.equals("No file Selected")){
            Toast.makeText(this, "please select a song!", Toast.LENGTH_SHORT).show();
        } else {
            if(mUploadsTask != null && mUploadsTask.isInProgress())
                Toast.makeText(this, "Uploading is already in progress!", Toast.LENGTH_SHORT).show();
            else
                uploadFiles();
        }
    }

    private void uploadFiles() {
        if(audioUri != null && filePath !=null){
            title.setEnabled(false);
            artist.setEnabled(false);
            album.setEnabled(false);
            dataa.setEnabled(false);
            final StorageReference sRef = storageReference.child(Constants.STORAGE_PATH_ART +
                    System.currentTimeMillis() + "."+getFileExtension(filePath));
            sRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            artist1 = artist.getText().toString();
                            title1 = title.getText().toString();
                            album_art1 = uri.toString();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            Toast.makeText(this, "uploading please wait!", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.VISIBLE);
            final StorageReference storageReference = mStorageRef.child(System.currentTimeMillis()+"."+getfileextention(audioUri));
            mUploadsTask = storageReference.putFile(audioUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            UploadSong uploadSong = new UploadSong(songsCategory,title1,artist1,album_art1,duration1,uri.toString());
                            String uploadId = referenceSongs.push().getKey();
                            referenceSongs.child(uploadId).setValue(uploadSong);
                            Toast.makeText(UploadActivity.this, title1 + " is successfully uploaded!", Toast.LENGTH_SHORT).show();
                            title.setEnabled(true);
                            artist.setEnabled(true);
                            album.setEnabled(true);
                            dataa.setEnabled(true);
                            title.getText().clear();
                            album.getText().clear();
                            artist.getText().clear();
                            dataa.getText().clear();
                            txtSongFileSelected.setText("No file Selected");
                            album_art.setImageResource(android.R.color.transparent);
                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                    progressBar.setProgress((int) progress);
                }
            });
        } else {
            Toast.makeText(this, "No file is selected to upload or no art uploaded", Toast.LENGTH_SHORT).show();
        }
    }

    private String getfileextention(Uri audioUri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(audioUri));
    }
    public String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getMimeTypeFromExtension(cr.getType(uri));
    }

    private void showFileChoose() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),PICK_IMAGE_REQUEST);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), bolt_pg.class);
        startActivity(intent);
        finish();
    }
}
