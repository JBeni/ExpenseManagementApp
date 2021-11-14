package com.example.project;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import java.util.ArrayList;
import java.util.List;

public class MainTripActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton add_button;
    TextView instructions_trip;

    SqliteDatabaseHandler databaseHandler;
    List<Trip> trips;
    RecyclerViewTripAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_main);

        recyclerView = findViewById(R.id.trip_recycler_view);
        add_button = findViewById(R.id.trip_add_main_button);
        instructions_trip = findViewById(R.id.instructions_trip);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainTripActivity.this, AddTripActivity.class);
                startActivity(intent);
            }
        });

        databaseHandler = new SqliteDatabaseHandler(MainTripActivity.this);
        trips = new ArrayList<Trip>();

        getTripsData();

        customAdapter = new RecyclerViewTripAdapter(MainTripActivity.this,this, trips);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(MainTripActivity.this, 3));

        /**
         * https://medium.com/@makkenasrinivasarao1/bottom-navigation-in-android-application-with-activities-material-design-7a056b8cf38
         ***/
        BottomNavigationView bottomNavigation = findViewById(R.id.navigation_bottom);
        bottomNavigation.setSelectedItemId(R.id.home_navigation);
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
            }
        });
    }

    private void getTripsData() {
        trips = databaseHandler.getAllTrips();
        if (trips.size() == 0) {
            instructions_trip.setVisibility(View.VISIBLE);
        } else {
            instructions_trip.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_data:
                confirmDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * https://www.geeksforgeeks.org/android-alert-dialog-box-and-how-to-create-it/
     */
    private void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainTripActivity.this);
        builder.setTitle("Delete Database Data");
        builder.setMessage("Are you sure you want to delete all the data from database?");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseHandler.deleteAllData();
                Intent intent = new Intent(MainTripActivity.this, MainTripActivity.class);
                startActivity(intent);
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
