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

import com.example.myapplication.AdapterAudio2.AudioAdapter2;
import com.example.myapplication.Audio;
import com.example.myapplication.Audio_penjelas;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AudioFragment extends Fragment {
    private EditText search_audio;
    private RecyclerView recyclerView;
    private AudioAdapter2 adapter;
    private ArrayList<Audio> audioArrayList;
    private DatabaseReference databaseReference;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_audio, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference("audios");

        recyclerView = view.findViewById(R.id.recycler6);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);

        audioArrayList = new ArrayList<>();
        adapter = new AudioAdapter2(getContext(), audioArrayList);
        adapter.setOnItemClickListener(new AudioAdapter2.OnItemClickListener() {
            @Override
            public void onClick(Audio audio) {
                // Retrieve the video URL from Firebase Storage
                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(audio.geturl());
                storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            int penonton = audio.getPenonton() + 1;
                            audio.setPenonton(penonton);
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("audio").child(audio.getjudul());
                            databaseReference.setValue(audio);
                            Intent intent = new Intent(getContext(), Audio_penjelas.class);
                            intent.putExtra("judul", audio.getjudul());
                            intent.putExtra("url", audio.geturl());
                            startActivity(intent);
                        } else {
                            Toast.makeText(getContext(), "Failed to retrieve video URL", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        recyclerView.setAdapter(adapter);

        search_audio = view.findViewById(R.id.audio);
        search_audio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchAudioList(s.toString());
            }
        });

        // Initial load of all audio data
        loadAllAudioData();
        return view;
    }

    private void loadAllAudioData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                audioArrayList.clear();
                for (com.google.firebase.database.DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Audio audio = snapshot.getValue(Audio.class);
                    audioArrayList.add(audio);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchAudioList(String keyword) {
        Query query = databaseReference.orderByChild("judul").startAt(keyword).endAt(keyword + "\uf8ff");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                audioArrayList.clear();
                for (com.google.firebase.database.DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Audio audio = snapshot.getValue(Audio.class);
                    audioArrayList.add(audio);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}