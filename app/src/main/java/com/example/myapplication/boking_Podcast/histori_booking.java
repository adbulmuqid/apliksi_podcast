package com.example.myapplication.boking_Podcast;

import static com.google.android.material.internal.ContextUtils.getActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.ProfilFragment;
import com.example.myapplication.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class histori_booking extends AppCompatActivity {

    FloatingActionButton button;
    private ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histori_booking);
        // ...
        button = findViewById(R.id.fab_add);
        back = findViewById(R.id.btn_bck);
        back.setOnClickListener(view -> onBackPressed());


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pindah ke activity lain saat tombol diklik
                Intent intent = new Intent(histori_booking.this, booking.class);
                startActivity(intent);
            }
        });


// Mendapatkan referensi RecyclerView dari layout XML
        RecyclerView recyclerView = findViewById(R.id.list_booking);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


// Membuat instance ArrayList untuk menyimpan data pengguna
        List<Data> bookinglist= new ArrayList<>();

// Membuat instance adapter dan mengatur ke RecyclerView
        bookingAdapter bookingAdapter = new bookingAdapter(this, bookinglist);
        recyclerView.setAdapter(bookingAdapter);

// ...
        FirebaseFirestore db = FirebaseFirestore.getInstance();
// Mengambil data dari Firestore
        db.collection("booking")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String nama = document.getString("nama");
                                String nomor_hp = document.getString("nomor_hp");
                                String durasi = document.getString("jam");  // Ganti dengan "durasi" jika sesuai
                                String tanggal = document.getString("tanggal");
                                String waktu = document.getString("waktu");

                                // Membuat instance Data dan menambahkannya ke ArrayList
                                Data booking = new Data(nama, nomor_hp, tanggal, waktu, durasi);
                                bookinglist.add(booking);

                                // Memberi tahu adapter bahwa data telah berubah
                                bookingAdapter.notifyDataSetChanged();
                            }

                        } else {
                            Log.w("TAG", "Gagal mengambil data", task.getException());
                        }
                    }
                });

    }
    @Override
    public void onBackPressed () {
        super.onBackPressed();
        finish();
    }
}