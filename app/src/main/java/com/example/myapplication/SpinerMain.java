package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class SpinerMain extends AppCompatActivity {
    Fragment fragment1Action; // Perbaiki penulisan nama Fragment
    Fragment fragment2Action; // Perbaiki penulisan nama Fragment
    Spinner spinner;
    List<String> names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spiner);

        // Inisialisasi spinner
        spinner = findViewById(R.id.Spiner);

        fragment1Action = new fragment1(); // Sesuaikan nama kelas Fragment1
        fragment2Action = new fragment2(); // Sesuaikan nama kelas Fragment2

        // Inisialisasi adapter dengan menggunakan sumber daya array
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.fragments));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) { // Menggunakan "position" bukan "i"
                    case 0:
                        setFragment(fragment1Action);
                        break;
                    case 1:
                        setFragment(fragment2Action);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Tidak melakukan apa-apa saat tidak ada yang dipilih
            }
        });
    }

    public void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }
}
