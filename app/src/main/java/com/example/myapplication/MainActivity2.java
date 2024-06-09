package com.example.myapplication;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

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

public class MainActivity2 extends AppCompatActivity {
    private  Button pilihbtn;
    private  static  final int PICK_VIDEO_REQUEST=1;
    private  Button uploudbtn;
    private VideoView videoView;
    private Uri videouri;
    MediaController mediaController;
    private StorageReference storageReference;

    private EditText judul;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        pilihbtn = findViewById(R.id.btn_pilih);
        uploudbtn = findViewById(R.id.btn_upload_video);
        videoView = findViewById(R.id.video_view);
        progressBar = findViewById(R.id.progress_bar);
        judul = findViewById(R.id.video_name);

        mediaController = new MediaController(this);

        storageReference = FirebaseStorage.getInstance().getReference("videos");


        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        videoView.start();

        pilihbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseVideo();
            }

        });
        uploudbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploudVideo();
            }
        });
    }

    private void UploudVideo() {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        if (videouri != null){
            StorageReference reference = storageReference.child(System.currentTimeMillis()+
                    "."+getfileExt(videouri));
            reference.putFile(videouri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Uploud berhasil", Toast.LENGTH_SHORT).show();

                    // Misalkan Anda memiliki koleksi "videos" di Firestore
                    DocumentReference docRef = database.collection("videos").document();

                    // Membuat objek member
                    Map<String, Object> member = new HashMap<>();
                    member.put("judul", judul.getText().toString().trim());
                    member.put("url", taskSnapshot.getUploadSessionUri().toString());

                    // Menambahkan data ke Firestore
                    docRef.set(member)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Data berhasil ditambahkan ke Firestore
                                    judul.setText(""); // Kosongkan field judul setelah berhasil
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

    private  void  ChooseVideo(){
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_VIDEO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null);
        videouri = data.getData();
        videoView.setVideoURI(videouri);
    }
    private  String getfileExt( Uri videouri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(videouri));
    }
}