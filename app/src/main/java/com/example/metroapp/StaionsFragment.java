package com.example.metroapp;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


/**
 * A simple {@link Fragment} subclass.
 */
public class StaionsFragment extends Fragment {
    private View v;
    private TabLayout tab;
    private ViewPager view;

    public StaionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_staions, container, false);
        initView();
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        view.setAdapter(new MyAdapter(getChildFragmentManager()));
        tab.setupWithViewPager(view);
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                view.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void initView() {
        tab = (TabLayout) v.findViewById(R.id.tab);
        view = (ViewPager) v.findViewById(R.id.view);


    }
    public class MyAdapter extends FragmentPagerAdapter {
        String date []={"Line 1","Line 2","Line 3"};
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position==0)
                return new FirstFragment() ;
            if(position==1)
                return new SecondFragment();
            if(position==2)
                return new ThirdFragment();
            return null;
        }

        @Override
        public int getCount() {
            return date.length;
        }

        @Override
        public CharSequence getPageTitle(int position){
            return date[position];
        }
    }


}
