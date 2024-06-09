package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter2 extends FragmentStateAdapter {


    public ViewPagerAdapter2(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
       switch (position){
           case  0 : return new HomePendengarFragment();
           case  1: return new PendengarPanduFragment();
           case 2: return new PendengarProfilFragment();
           default: return new HomePendengarFragment();
       }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
