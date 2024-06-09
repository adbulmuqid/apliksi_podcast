package com.example.myapplication.tab_fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyViewAdapter extends FragmentStateAdapter {
    public MyViewAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new VideoFragment();
            case 1:
                return new AudioFragment();
            default:
                return new VideoFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
