package com.example.deskbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainFragmentActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static String bookDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment);

        BottomNavigationView navigationView = findViewById(R.id.Bottom_Navigation);
        navigationView.setOnNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        int check = intent.getIntExtra("check",0);
        //if intent came from edit user profile
        if(check == 1){
            loadFragment(new ProfileFragment());
        }
        else {
            loadFragment(new HomeFragment());
        }
    }

    private boolean loadFragment(Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_Container, fragment).commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        switch (menuItem.getItemId()){
            case R.id.MenuHome:
                fragment = new HomeFragment();
                break;
            case R.id.MenuHistory:
                fragment = new HistoryFragment();
                break;
            case R.id.MenuProfile:
                fragment = new ProfileFragment();
                break;
        }
        return loadFragment(fragment);

    }
}
