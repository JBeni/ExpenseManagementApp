package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SearchActivity extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        BottomNavigationView bottomNavigation = findViewById(R.id.navigation_bottom);
        bottomNavigation.setSelectedItemId(R.id.search_navigation);
        bottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home_navigation:
                    startActivity(new Intent(getApplicationContext(), MainTripActivity.class));
                    break;
                case R.id.search_navigation:
                    startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                    break;
                case R.id.settings_navigation:
                    startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                    break;
            }
            return true;
        });
    }
}
