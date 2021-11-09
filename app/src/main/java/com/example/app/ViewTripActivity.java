package com.example.app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ViewTripActivity extends AppCompatActivity {

    TextView name, destination, date, risk_assessment, description, duration, aim, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trip);

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
    }
}
