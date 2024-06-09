package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class home_pendengar extends AppCompatActivity  {
    BottomNavigationView bottomNavigationView;
    ViewPager2 viewPager2;
    ViewPagerAdapter2 viewPagerAdapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_pendengar);
        bottomNavigationView = findViewById(R.id.bottomNavView2);
        viewPager2=findViewById(R.id.viewPager2);
        viewPagerAdapter2 = new ViewPagerAdapter2(this);
        viewPager2.setAdapter(viewPagerAdapter2);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.Bottomhome2:
                        viewPager2.setCurrentItem(0);
                        break;
                    case R.id.uplod2:
                        viewPager2.setCurrentItem(1);
                        break;
                    case R.id.profile2:
                        viewPager2.setCurrentItem(2);
                        break;
                }
                return false;
            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.Bottomhome2).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.uplod2).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.profile2).setChecked(true);
                        break;
                }
                super.onPageSelected(position);
            }
    });

    }
}