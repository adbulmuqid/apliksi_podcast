package com.example.myapplication;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class listialat extends AppCompatActivity {

    TextView judul, isi;
    ImageView gambar;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listalat);
        judul = findViewById(R.id.singlejudul);
        isi = findViewById(R.id.singleisi);
        back = findViewById(R.id.btn_bck);
        back.setOnClickListener(view -> onBackPressed());
        gambar = findViewById(R.id.imagesingle);

        Picasso.get().load(getIntent().getStringExtra("singleImage"))
                .placeholder(R.drawable.gambar)
                .into(gambar);
        judul.setText(getIntent().getStringExtra("singlejudul"));
        isi.setText(getIntent().getStringExtra("singleisi"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}