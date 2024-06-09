package com.example.myapplication;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.myapplication.boking_Podcast.booking;
import com.example.myapplication.boking_Podcast.histori_booking;
import com.example.myapplication.item_home.panduActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private CardView btn_uplod,btn_lihat,btn_uploud_audio,btn_uploud_video,btn_pandu,btn_boking;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ImageSlider imageSlider =view.findViewById(R.id.viewSlide);
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.beneropen, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.banner_apk2, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.banner_apk3, ScaleTypes.FIT));

        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

        btn_pandu= view.findViewById(R.id.cardView1);
        btn_pandu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pindah ke activity lain saat tombol diklik
                Intent intent = new Intent(getActivity(), panduActivity.class);
                startActivity(intent);
            }
        });

        btn_uploud_audio = view.findViewById(R.id.cardView3);
        btn_uploud_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pindah ke activity lain saat tombol diklik
                Intent intent = new Intent(getActivity(), Upload_audio.class);
                startActivity(intent);
            }
        });
        btn_uploud_video = view.findViewById(R.id.cardView2);
        btn_uploud_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pindah ke activity lain saat tombol diklik
                Intent intent = new Intent(getActivity(), Upload_video.class);
                startActivity(intent);
            }
        });
        btn_lihat= view.findViewById(R.id.cardView4);
        btn_lihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pindah ke activity lain saat tombol diklik
                Intent intent = new Intent(getActivity(), MainActivity4.class);
                startActivity(intent);
            }
        });
        btn_boking= view.findViewById(R.id.cardView5);
        btn_boking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pindah ke activity lain saat tombol diklik
                Intent intent = new Intent(getActivity(), histori_booking.class);
                startActivity(intent);
            }
        });

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