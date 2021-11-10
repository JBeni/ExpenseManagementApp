package com.example.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainTripActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton add_button;
    ImageView empty_imageview;
    TextView no_data;

    SqliteDatabaseHandler databaseHandler;
    List<Trip> trips;
    RecyclerViewTripAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_main);

        recyclerView = findViewById(R.id.trip_recycler_view);
        add_button = findViewById(R.id.trip_add_main_button);
        empty_imageview = findViewById(R.id.empty_imageview);
        no_data = findViewById(R.id.no_data);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainTripActivity.this, AddTripActivity.class);
                startActivity(intent);
            }
        });

        databaseHandler = new SqliteDatabaseHandler(MainTripActivity.this);
        trips = new ArrayList<Trip>();

        storeDataInArrays();

        customAdapter = new RecyclerViewTripAdapter(MainTripActivity.this,this, trips);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(MainTripActivity.this, 3));
    }

    /**
     * from https://github.com/stevdza-san/SQLite_Android-Complete_Tutorial/blob/master/app/src/main/java/com/jovanovic/stefan/sqlitetutorial/MainActivity.java
     */
    void storeDataInArrays() {
        trips = databaseHandler.getAllTrips();
        if (trips.size() == 0) {
            empty_imageview.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);
        } else {
            empty_imageview.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);
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
    void confirmDialog() {
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
