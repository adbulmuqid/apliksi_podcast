package com.example.myapplication;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class Audio_penjelas extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private TextView judul;
    private SeekBar seekBar;
    private TextView currentTimeTextView;
    private TextView totalTimeTextView;
    private ImageView pausePlayImageView;
    private ImageView back;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_penjelas);

        // Initialize UI elements
        seekBar = findViewById(R.id.seek_bar);
        currentTimeTextView = findViewById(R.id.current_time);
        totalTimeTextView = findViewById(R.id.total_time);
        pausePlayImageView = findViewById(R.id.pause_play);
        judul = findViewById(R.id.song_title);
        judul.setText(getIntent().getStringExtra("judul"));
        back = findViewById(R.id.btn_bck);
        back.setOnClickListener(view -> onBackPressed());
        String audioUrl = getIntent().getStringExtra("url");

        // Initialize MediaPlayer
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build());

        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Replace with your audio file

        // Set total time text
        totalTimeTextView.setText(formatTime(mediaPlayer.getDuration()));

        // Set up SeekBar
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // Update SeekBar and current time text
        updateSeekBar();

        // Set click listener for play/pause button
        pausePlayImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPlayPauseClick(view);
            }
        });
    }

    private void updateSeekBar() {
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        currentTimeTextView.setText(formatTime(mediaPlayer.getCurrentPosition()));

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateSeekBar();
            }
        }, 1000);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void onPlayPauseClick(View view) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            pausePlayImageView.setImageResource(R.drawable.baseline_play_circle_outline_24);
        } else {
            mediaPlayer.start();
            pausePlayImageView.setImageResource(R.drawable.baseline_pause_circle_outline_24);
        }
    }

    private String formatTime(int millis) {
        int seconds = millis / 1000;
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%d:%02d", minutes, remainingSeconds);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        handler.removeCallbacksAndMessages(null);
    }
}