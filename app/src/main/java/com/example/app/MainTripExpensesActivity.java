package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class MainTripExpensesActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton add_button;
    TextView instructions_expense;

    SqliteDatabaseHandler databaseHandler;
    List<Expense> expenses;
    RecyclerViewExpenseAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_main);

        recyclerView = findViewById(R.id.expense_recycler_view);
        add_button = findViewById(R.id.expense_add_main_button);
        instructions_expense = findViewById(R.id.instructions_expense);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainTripExpensesActivity.this, AddExpenseActivity.class);
                intent.putExtra("trip_id", String.valueOf(getIntent().getStringExtra("trip_id")));
                startActivity(intent);
            }
        });

        databaseHandler = new SqliteDatabaseHandler(MainTripExpensesActivity.this);
        expenses = new ArrayList<Expense>();

        getTripExpensesData();

        customAdapter = new RecyclerViewExpenseAdapter(MainTripExpensesActivity.this,this, expenses);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(MainTripExpensesActivity.this, 3));

        BottomNavigationView bottomNavigation = findViewById(R.id.navigation_bottom);
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

    private void getTripExpensesData() {
        SharedTripViewModel sharedTripView = (SharedTripViewModel) getApplicationContext();
        String trip_id = sharedTripView.getSharedTripId();

        expenses = databaseHandler.getTripExpenses(trip_id);
        if (expenses.size() == 0) {
            instructions_expense.setVisibility(View.VISIBLE);
        } else {
            instructions_expense.setVisibility(View.GONE);
        }
    }
}
