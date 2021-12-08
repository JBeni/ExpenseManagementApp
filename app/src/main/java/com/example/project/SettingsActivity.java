package com.example.project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingsActivity extends AppCompatActivity {
    TextView instructionsTrip, instructionsExpense, instructionsSearch;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        instructionsTrip = findViewById(R.id.instructions_trip_page);
        instructionsExpense = findViewById(R.id.instructions_expense_page);
        instructionsSearch = findViewById(R.id.instructions_search_page);

        instructionsTrip.setOnClickListener(view -> {
            String message = getResources().getString(R.string.instructions_trip_information);
            dialogInstructions("Instructions Trip", message);
        });
        instructionsExpense.setOnClickListener(view -> {
            String message = getResources().getString(R.string.instructions_expense_information);
            dialogInstructions("Instructions Expense", message);
        });
        instructionsSearch.setOnClickListener(view -> {
            String message = getResources().getString(R.string.instructions_search_information);
            dialogInstructions("Instructions Search", message);
        });

        // https://medium.com/@makkenasrinivasarao1/bottom-navigation-in-android-application-with-activities-material-design-7a056b8cf38
        BottomNavigationView bottomNavigation = findViewById(R.id.navigation_bottom);
        bottomNavigation.setSelectedItemId(R.id.settings_navigation);
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

    public void dialogInstructions(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton("Close Dialog", (dialog, which) -> dialog.cancel());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
