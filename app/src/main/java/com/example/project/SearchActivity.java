package com.example.project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

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
            trips.clear();

            boolean b = trips.removeAll(trips);
            int sizV = trips.size();

            search_name_enter = findViewById(R.id.search_text_view_field);

            if (search_name_enter.getText().toString().trim().length() == 0) {
                getTripsData();
                int sizVas = trips.size();
            } else {
                getSearchTripsData();
            }

            customSearchAdapter = new RecyclerViewSearchTripAdapter(SearchActivity.this,this, trips);
            recyclerView.setAdapter(customSearchAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        });
        reset_button.setOnClickListener(view -> {
            clearTripsData();
            setRecycler();
        });

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
            }
            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_advanced, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_advance_item:
                advancedSearchDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void advancedSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
        builder.setTitle("Advance Search Filter");
        builder.setCancelable(false);

        Intent intent = new Intent(SearchActivity.this, AdvanceSearchActivity.class);
        startActivity(intent);

        builder.setMessage("Are you sure you want to delete all the data from database?");

        builder.setPositiveButton("Search", (dialog, which) -> {
            finish();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void setRecycler() {
        customSearchAdapter = new RecyclerViewSearchTripAdapter(SearchActivity.this,this, trips);
        recyclerView.setAdapter(customSearchAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
    }

    private void getSearchTripsData() {
        trips = new ArrayList<>();
        trips = databaseHandler.getAllTrips();
    }

    private void getTripsData() {
        trips = new ArrayList<>();
        trips = databaseHandler.getAllTrips();
    }

    private void clearTripsData() {
        trips = new ArrayList<>();
        customSearchAdapter = new RecyclerViewSearchTripAdapter(SearchActivity.this,this, trips);
    }


}
