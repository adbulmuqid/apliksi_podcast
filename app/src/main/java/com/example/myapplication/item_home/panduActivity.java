package com.example.myapplication.item_home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapter;
import com.example.myapplication.tips;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class panduActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private ImageButton back;
    ArrayList<tips> recyclelist;
    FirebaseDatabase firebaseDatabase;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pandu);

        recyclerView = findViewById(R.id.list_item);
        recyclelist = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();

        Context context = getApplicationContext(); // Mendapatkan konteks Fragment

        adapter recyclerAdapter = new adapter(recyclelist, context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context); // Menggunakan konteks Fragment
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(recyclerAdapter);
        back = findViewById(R.id.btn_bck);
        back.setOnClickListener(view -> onBackPressed());

        firebaseDatabase.getReference().child("tips").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    tips model = dataSnapshot.getValue(tips.class);
                    recyclelist.add(model);
                }
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "gagal: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Metode onBackPressed di luar dari onCreate
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}