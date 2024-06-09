package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TableLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;


import com.example.myapplication.tab_fragment.MyViewAdapter;
import com.google.android.material.tabs.TabLayout;


public class PendengarPanduFragment extends Fragment {
TabLayout tabLayout;
ViewPager2 viewPager2;
MyViewAdapter myViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pendengar_pandu, container, false);

        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager2 = view.findViewById(R.id.view_pager3);
        myViewAdapter = new MyViewAdapter(this);

        viewPager2.setAdapter(myViewAdapter);

tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager2.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
});
        return view;
    }
}