package com.example.project.trip;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.example.project.MainTripActivity;
import com.example.project.R;
import com.example.project.SearchActivity;
import com.example.project.SettingsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ViewTripActivity extends AppCompatActivity {
    TextView name, destination, date, risk_assessment, description, duration, aim, status;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_view_activity);

        name = findViewById(R.id.view_name_trip_column);
        destination = findViewById(R.id.view_destination_trip_column);
        date = findViewById(R.id.view_date_trip_column);
        risk_assessment = findViewById(R.id.view_risk_assessment_trip_column);
        description = findViewById(R.id.view_description_trip_column);
        duration = findViewById(R.id.view_duration_trip_column);
        aim = findViewById(R.id.view_aim_trip_column);
        status = findViewById(R.id.view_status_trip_column);

        if (getIntent().hasExtra("id") && getIntent().hasExtra("name") &&
                getIntent().hasExtra("destination") && getIntent().hasExtra("date") &&
                getIntent().hasExtra("risk_assessment") && getIntent().hasExtra("duration") &&
                getIntent().hasExtra("status")
        ) {
            name.setText(getIntent().getStringExtra("name"));
            destination.setText(getIntent().getStringExtra("destination"));
            date.setText(getIntent().getStringExtra("date"));
            risk_assessment.setText(getIntent().getStringExtra("risk_assessment"));
            description.setText(getIntent().getStringExtra("description"));
            duration.setText(getIntent().getStringExtra("duration"));
            aim.setText(getIntent().getStringExtra("aim"));
            status.setText(getIntent().getStringExtra("status"));
        }

        // https://medium.com/@makkenasrinivasarao1/bottom-navigation-in-android-application-with-activities-material-design-7a056b8cf38
        BottomNavigationView bottomNavigation = findViewById(R.id.navigation_bottom);
        bottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home_navigation:
                    startActivity(new Intent(getApplicationContext(), MainTripActivity.class));
                    break;
                case R.id.settings_navigation:
                    startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                    break;
                case R.id.search_navigation:
                    startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                    break;
            }
            return true;
        });
    }
}
