package com.example.myapplication;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

public class Video_penjelas extends AppCompatActivity {

    TextView judul;
    private ImageButton back;
    SimpleExoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_penjelas);
        back = findViewById(R.id.btn_bck);
        back.setOnClickListener(view -> onBackPressed());
        judul = findViewById(R.id.judul);
        judul.setText(getIntent().getStringExtra("judul"));
        player = new SimpleExoPlayer.Builder(Video_penjelas.this).build();
        Video videourl = new Video();
        PlayerView playerView = findViewById(R.id.video);
        playerView.setPlayer(player);
        String uri = videourl.seturl(getIntent().getStringExtra("url"));
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(
                new DefaultDataSourceFactory(Video_penjelas.this, "exoplayer-sample"))
                .createMediaSource(MediaItem.fromUri(Uri.parse(uri)));
        player.setMediaSource(mediaSource);
        player.prepare();
        player.setPlayWhenReady(true); // Memulai pemutaran otomatis
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        releasePlayer();
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }
}