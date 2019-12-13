package com.example.metroapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class HomeActivity extends AppCompatActivity {

    TabLayout tabLayout;
    private FrameLayout frame;
    private BottomNavigationView navigation;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        loadFragment(new StaionsFragment());
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.staions:
                        loadFragment(new StaionsFragment());
                        return true;
                    case R.id.metro_map:
                        loadFragment(new Metro_Map_Fragment());
                        return true;
                    case R.id.booking:
                        loadFragment(new BookingFragment());
                        return true;
                    case R.id.ticket:
                        loadFragment(new MyTicketFragment());
                        return true;
                    default:
                        return true;
                }
            }
        });

    }

    private void initView() {
        frame = (FrameLayout) findViewById(R.id.frame);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        fragmentManager=getSupportFragmentManager();

    }
    public void loadFragment(Fragment fragment){
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment);
        fragmentTransaction.commit();
    }
}






