package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainTripExpensesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton add_button;
    ImageView empty_imageview;
    TextView no_data;

    SqliteDatabaseHandler databaseHandler;
    List<Expense> expenses;
    RecyclerViewExpenseAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_main);

        recyclerView = findViewById(R.id.expense_recycler_view);
        add_button = findViewById(R.id.expense_add_main_button);
        empty_imageview = findViewById(R.id.empty_imageview);
        no_data = findViewById(R.id.no_data);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainTripExpensesActivity.this, AddExpenseActivity.class);
                intent.putExtra("trip_id", getIntent().getStringExtra("trip_id"));
                startActivity(intent);
            }
        });

        databaseHandler = new SqliteDatabaseHandler(MainTripExpensesActivity.this);
        expenses = new ArrayList<Expense>();

        storeDataInArrays();

        customAdapter = new RecyclerViewExpenseAdapter(MainTripExpensesActivity.this,this, expenses);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(MainTripExpensesActivity.this, 3));
    }

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 1) {
            if (data.hasExtra("trip_id")) {
                Toast.makeText(this, data.getExtras().getString("trip_id"),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
    */


    /**
     * Code taken from https://github.com/stevdza-san/SQLite_Android-Complete_Tutorial/blob/master/app/src/main/java/com/jovanovic/stefan/sqlitetutorial/MainActivity.java
     */
    void storeDataInArrays() {
        SharedTripViewModel sharedTripView = (SharedTripViewModel) getApplicationContext();
        String trip_id = sharedTripView.getSharedTripId();

        expenses = databaseHandler.getTripExpenses(trip_id);
        if (expenses.size() == 0) {
            empty_imageview.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);
        } else {
            empty_imageview.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);
        }
    }
}