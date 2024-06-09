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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class MainActivity3 extends AppCompatActivity {
    private Button pilihBtn;
    private static final int PICK_AUDIO_REQUEST = 1;
    private Button uploadBtn;
    private TextView audioTitle; // Mengganti VideoView menjadi TextView
    private Uri audioUri;
    private StorageReference storageReference;
    private ProgressBar progressBar;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        pilihBtn = findViewById(R.id.btn_pilih);
        uploadBtn = findViewById(R.id.btn_upload_audio); // Mengganti ID
        audioTitle = findViewById(R.id.audio_name); // Mengganti ID
        progressBar = findViewById(R.id.progress);

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
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        progressBar.setVisibility(View.VISIBLE);
        if (audioUri != null) {
            StorageReference reference = storageReference.child(System.currentTimeMillis() +
                    "." + getFileExtension(audioUri));
            reference.putFile(audioUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.GONE);
                    String title = audioTitle.getText().toString().trim();

                    // Misalkan Anda memiliki koleksi "audios" di Firestore
                    Map<String, Object> audio = new HashMap<>();
                    audio.put("judul", title);
                    audio.put("url", taskSnapshot.getUploadSessionUri().toString());

                    // Menambahkan data ke Firestore
                    database.collection("audios")
                            .add(audio)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    // Data berhasil ditambahkan ke Firestore
                                    Toast.makeText(getApplicationContext(), "Unggahan berhasil", Toast.LENGTH_SHORT).show();
                                    audioTitle.setText(""); // Mengosongkan judul setelah unggahan berhasil
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Gagal menambahkan data ke Firestore
                                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Tidak ada file yang dipilih", Toast.LENGTH_SHORT).show();
        }
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
            audioTitle.setText(getFileName(audioUri));
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