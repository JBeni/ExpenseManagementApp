package com.example.project;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.example.project.models.Trip;
import com.example.project.recycler.RecyclerViewSearchTripAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Button search_button, reset_button;
    EditText search_name_enter;

    SqliteDatabaseHandler databaseHandler;
    List<Trip> trips;
    RecyclerViewSearchTripAdapter customSearchAdapter;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerView = findViewById(R.id.search_trip_recycler_view);
        search_button = findViewById(R.id.search_trip_db_button);
        reset_button = findViewById(R.id.reset_search_trip_button);
        databaseHandler = new SqliteDatabaseHandler(SearchActivity.this);

        trips = new ArrayList<>();
        customSearchAdapter = new RecyclerViewSearchTripAdapter(SearchActivity.this,this, trips);
        recyclerView.setAdapter(customSearchAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        search_button.setOnClickListener(view -> {
            search_name_enter = findViewById(R.id.search_text_view_field);

            if (search_name_enter.getText().toString().trim().length() == 0) {
                getTripsWithoutFilter();
            } else {
                getSimpleSearchFilter(search_name_enter.getText().toString().trim());
            }

            setRecyclerViewCustomAdapter();
        });
        reset_button.setOnClickListener(view -> clearTripsData());

        // https://medium.com/@makkenasrinivasarao1/bottom-navigation-in-android-application-with-activities-material-design-7a056b8cf38
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

    // https://stackoverflow.com/questions/61455381/how-to-replace-startactivityforresult-with-activity-result-apis
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();

                if (
                    Objects.requireNonNull(data).hasExtra("advance_search_name") ||
                    Objects.requireNonNull(data).hasExtra("advance_search_destination") ||
                    Objects.requireNonNull(data).hasExtra("advance_search_destination")
                ) {
                    String name = data.getStringExtra("advance_search_name");
                    String destination = data.getStringExtra("advance_search_destination");
                    String date = data.getStringExtra("advance_search_date");

                    getAdvanceSearchFilter(name, destination, date);
                }
            }
        });

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_advanced, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != R.id.search_advance_item) {
            return super.onOptionsItemSelected(item);
        }
        Intent intent = new Intent(SearchActivity.this, AdvanceSearchActivity.class);
        someActivityResultLauncher.launch(intent);
        return true;
    }

    private void getAdvanceSearchFilter(String name, String destination, String date) {
        trips = databaseHandler.getAdvanceSearchFilter(name, destination, date);
        if (trips.size() == 0) {
            displayMessageFilter();
        } else {
            setRecyclerViewCustomAdapter();
        }
    }

    private void getSimpleSearchFilter(String name) {
        trips = databaseHandler.getSimpleSearchFilter(name);
        if (trips.size() == 0) {
            displayMessageFilter();
        }
    }

    public void displayMessageFilter() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
        builder.setCancelable(false);
        builder.setTitle("Search Filter");
        builder.setMessage("There was no data with the value entered found in the database.");

        builder.setNegativeButton("Close Dialog", (dialog, which) -> dialog.cancel());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getTripsWithoutFilter() {
        trips = databaseHandler.getAllTrips();
    }

    private void setRecyclerViewCustomAdapter() {
        customSearchAdapter = new RecyclerViewSearchTripAdapter(SearchActivity.this,this, trips);
        recyclerView.setAdapter(customSearchAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
    }

    private void clearTripsData() {
        trips = new ArrayList<>();
        setRecyclerViewCustomAdapter();
    }
}
