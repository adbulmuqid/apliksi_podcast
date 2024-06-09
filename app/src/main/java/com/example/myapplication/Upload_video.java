package com.example.myapplication;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class Upload_video extends AppCompatActivity {
    private  Button pilihbtn;
    private  static  final int PICK_VIDEO_REQUEST=1;
    private  Button uploudbtn;
    private VideoView videoView;
   private ImageButton back;
    private Uri videouri;
    MediaController mediaController;
    private StorageReference storageReference;

    private EditText judul;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_video);

        pilihbtn = findViewById(R.id.btn_pilih);
        uploudbtn = findViewById(R.id.btn_upload_video);
        videoView = findViewById(R.id.video_view);
        progressBar = findViewById(R.id.progress_bar);
        judul = findViewById(R.id.video_name);
        back = findViewById(R.id.btn_bck);
        back.setOnClickListener(view -> onBackPressed());

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
                uploadVideo();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void uploadVideo() {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        if (videouri != null) {
            StorageReference reference = firebaseStorage.getReference().child("video")
                    .child(System.currentTimeMillis() + "");

            reference.putFile(videouri).addOnSuccessListener(taskSnapshot -> {
                progressBar.setVisibility(View.GONE);

                reference.getDownloadUrl().addOnSuccessListener(uri -> {
                    // Use the obtained download URL directly
                    String downloadUrl = uri.toString();
                    int penonton = 0;
                    Map<String, Object> member = new HashMap<>();
                    member.put("judul", judul.getText().toString().trim());
                    member.put("url", downloadUrl);
                    member.put("penonton", penonton);

                    firebaseDatabase.getReference().child("videos").child(String.valueOf(judul.getText())).setValue(member)
                            .addOnSuccessListener(unused -> {
                                Toast.makeText(Upload_video.this, "Berhasil upload", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(Upload_video.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                });
            }).addOnFailureListener(e -> {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            Toast.makeText(getApplicationContext(), "Tidak ada file yang dipilih", Toast.LENGTH_SHORT).show();
        }
    }




    private void ChooseVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_VIDEO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            videouri = data.getData();
            videoView.setVideoURI(videouri);
        }
    }
    private  String getfileExt( Uri videouri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(videouri));
    }
}
