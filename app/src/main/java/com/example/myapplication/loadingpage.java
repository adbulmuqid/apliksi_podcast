package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class loadingpage extends AppCompatActivity {
    private static final int SPLASH_TIMEOUT = 1000;
    private static final String PREF_NAME = "MyPreferences";
    private static final String KEY_FIRST_TIME_DOWNLOAD = "firstTimeDownload";
    private static final String KEY_LOGIN_STATUS = "loginStatus";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadingpage);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                boolean firstTimeDownload = preferences.getBoolean(KEY_FIRST_TIME_DOWNLOAD, true);

                if (firstTimeDownload) {
                    // Aplikasi pertama kali diunduh, arahkan ke halaman Onboarding
                    Intent intent = new Intent(loadingpage.this, onboarding1.class);
                    startActivity(intent);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(KEY_FIRST_TIME_DOWNLOAD, false);
                    editor.apply();
                    finish();
                } else {
                    // Aplikasi sudah pernah diunduh, cek status login
                    boolean isLoggedIn = preferences.getBoolean(KEY_LOGIN_STATUS, false);

                    if (isLoggedIn) {
                        // Pengguna sudah login, arahkan ke halaman menu A
                        Intent intent = new Intent(loadingpage.this, home_pembicara.class);
                        startActivity(intent);
                    } else {
                        // Pengguna belum login, arahkan ke halaman menu B
                        Intent intent = new Intent(loadingpage.this, home_pendengar.class);
                        startActivity(intent);
                    }
                    finish();
                }
            }
        }, SPLASH_TIMEOUT);
    }
}