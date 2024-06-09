package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;


public class HomePendengarFragment extends Fragment {





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_pendengar, container, false);
       ImageSlider imageSlider =view.findViewById(R.id.viewSlide2);
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.banner_apk2, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.banner_apk3, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.beneropen, ScaleTypes.FIT));

        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("videos");
        RecyclerView recyclerView = view.findViewById(R.id.recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        final ArrayList<Video> arrayList = new ArrayList<>();
        final VideoAdapter adapter = new VideoAdapter(getActivity(), arrayList);
        adapter.setOnItemClickListener(new VideoAdapter.OnItemClickListener() {

            @Override
            public void onClick(Video video) {
                // Retrieve the video URL from Firebase Storage
                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(video.geturl());
                storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(requireContext(), Video_penjelas.class);
                            intent.putExtra("judul",video.getjudul());
                            intent.putExtra("url", video.geturl());
                            startActivity(intent);
                        } else {
                            Toast.makeText(getContext(), "Failed to retrieve video URL", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
        recyclerView.setAdapter(adapter);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (com.google.firebase.database.DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Video video = snapshot.getValue(Video.class);
                    arrayList.add(video);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull com.google.firebase.database.DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });

        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        DatabaseReference refreReference = database1.getReference("audios");

        RecyclerView recycleraudio = view.findViewById(R.id.recycler2);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recycleraudio.setLayoutManager(layoutManager1);

        final ArrayList<Audio> audioArrayList = new ArrayList<>();
        final AudioAdapter adapter1 = new AudioAdapter(getActivity(), audioArrayList);
        adapter1.setOnItemClickListener(new AudioAdapter.OnItemClickListener() {

            @Override
            public void onClick(Audio audio) {
                StorageReference storageReference1 = FirebaseStorage.getInstance().getReferenceFromUrl(audio.geturl());
                storageReference1.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task1) {
                        if (task1.isSuccessful()) {
                            int penonton = audio.getPenonton() + 1;
                            audio.setPenonton(penonton);
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("audio").child(audio.getjudul());
                            databaseReference.setValue(audio);
                            Intent intent = new Intent(getActivity(), Audio_penjelas.class);
                            intent.putExtra("judul",audio.getjudul());
                            intent.putExtra("url", audio.geturl());
                            startActivity(intent);
                        } else {
                            Toast.makeText(getActivity(), "Failed to retrieve video URL", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        recycleraudio.setAdapter(adapter1);

        refreReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                audioArrayList.clear();
                for (com.google.firebase.database.DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Audio audio = snapshot.getValue(Audio.class);
                    audioArrayList.add(audio);
                }
                Collections.sort(audioArrayList, (audio1, audio2) -> Integer.compare(audio2.getPenonton(), audio1.getPenonton()));

                int listSize = Math.min(audioArrayList.size(), 5);
                ArrayList<Audio> top5Audio = new ArrayList<>(audioArrayList.subList(0, listSize));
                adapter1.setData(top5Audio);
            }

            @Override
            public void onCancelled(@NonNull com.google.firebase.database.DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    // Inflate the layout for this fragment


}