package com.example.myapplication.tab_fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.Video;
import com.example.myapplication.Video_penjelas;
import com.example.myapplication.videoAdapter2.VideoAdapterList;
import com.example.myapplication.videoAdapter2.Videoactiv;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class VideoFragment extends Fragment {

    private EditText search_video;
    private RecyclerView recyclerView;
    private VideoAdapterList adapter;
    private ArrayList<Video> videoArrayList;
    private DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference("videos");

        recyclerView = view.findViewById(R.id.recycler4);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());

        recyclerView.setLayoutManager(layoutManager);

        videoArrayList = new ArrayList<>();
        adapter = new VideoAdapterList(getContext(), videoArrayList);
        adapter.setOnItemClickListener(new VideoAdapterList.OnItemClickListener() {

            @Override
            public void onClick(Video video) {
                // Retrieve the video URL from Firebase Storage
                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(video.geturl());
                storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            int penonton = video.getPenonton() + 1;
                            video.setPenonton(penonton);
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("videos").child(video.getjudul());
                            databaseReference.setValue(video);
                            Intent intent = new Intent(requireContext(), Video_penjelas.class);
                            intent.putExtra("judul", video.getjudul());
                            intent.putExtra("url", video.geturl());
                            startActivity(intent);
                        } else {
                            Toast.makeText(requireContext(), "Conection Filed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        recyclerView.setAdapter(adapter);

        search_video = view.findViewById(R.id.search_video);
        search_video.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchVideoList(s.toString());
            }
        });

        // Initial load of all video data
        loadAllVideoData();

        return view;
    }

    private void loadAllVideoData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                videoArrayList.clear();
                for (com.google.firebase.database.DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Video video = snapshot.getValue(Video.class);
                    videoArrayList.add(video);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull com.google.firebase.database.DatabaseError databaseError) {
                Toast.makeText(requireContext(), "Failed to load data", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void searchVideoList(String keyword) {
        Query query = databaseReference.orderByChild("judul").startAt(keyword).endAt(keyword + "\uf8ff");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                videoArrayList.clear();
                for (com.google.firebase.database.DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Video video = snapshot.getValue(Video.class);
                    videoArrayList.add(video);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull com.google.firebase.database.DatabaseError databaseError) {
                Toast.makeText(requireContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
                // Tidak perlu menggunakan return view; di sini
            }

        });
    }
}