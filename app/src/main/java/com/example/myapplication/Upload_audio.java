package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class Upload_audio extends AppCompatActivity {
    private Button pilihBtn;
    private static final int PICK_AUDIO_REQUEST = 1;
    private Button uploadBtn;
    private EditText audioTitle;
    private TextView audio;// Mengganti VideoView menjadi TextView
    private Uri audioUri;
    private ImageButton back;
    private StorageReference storageReference;
    private ProgressBar progressBar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_audio);

        pilihBtn = findViewById(R.id.btn_pilih);
        uploadBtn = findViewById(R.id.btn_upload_audio); // Mengganti ID
        audioTitle = findViewById(R.id.audio_name); // Mengganti ID
        audio = findViewById(R.id.audio);
        progressBar = findViewById(R.id.progress);
        back = findViewById(R.id.btn_bck);
        back.setOnClickListener(view -> onBackPressed());

        storageReference = FirebaseStorage.getInstance().getReference("audios");


        pilihBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseAudio();
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadAudio();
            }
        });

    }

    private void uploadAudio() {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        if (audioUri != null) {
            StorageReference reference = firebaseStorage.getReference().child("video")
                    .child(System.currentTimeMillis() + "");

            reference.putFile(audioUri).addOnSuccessListener(taskSnapshot -> {
                progressBar.setVisibility(View.GONE);

                reference.getDownloadUrl().addOnSuccessListener(uri -> {
                    // Use the obtained download URL directly
                    String downloadUrl = uri.toString();
                    int penonton = 0;
                    Map<String, Object> member = new HashMap<>();
                    member.put("judul", audioTitle.getText().toString().trim());
                    member.put("url", downloadUrl);
                    member.put("penonton", penonton);

                    firebaseDatabase.getReference().child("audios").child(String.valueOf(audioTitle.getText())).setValue(member)
                            .addOnSuccessListener(unused -> {
                                Toast.makeText(Upload_audio.this, "Berhasil upload", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(Upload_audio.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                });
            }).addOnFailureListener(e -> {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            Toast.makeText(getApplicationContext(), "Tidak ada file yang dipilih", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    private void chooseAudio() {
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_AUDIO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_AUDIO_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            audioUri = data.getData();
            // Menampilkan nama audio dari URI yang dipilih ke dalam TextView
            audio.setText(getFileName(audioUri));
        }
    }

    private String getFileExtension(Uri audioUri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(audioUri));
    }

    @SuppressLint("Range")
    private String getFileName(Uri audioUri) {
        String result = null;
        if (audioUri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(audioUri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = audioUri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}