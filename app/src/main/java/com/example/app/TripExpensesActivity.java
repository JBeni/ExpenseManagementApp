package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class TripExpensesActivity extends AppCompatActivity {

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
                Intent intent = new Intent(TripExpensesActivity.this, AddExpenseActivity.class);
                intent.putExtra("trip_id", getIntent().getStringExtra("trip_id"));
                startActivity(intent);
            }
        });

        databaseHandler = new SqliteDatabaseHandler(TripExpensesActivity.this);
        expenses = new ArrayList<Expense>();

        storeDataInArrays();

        customAdapter = new RecyclerViewExpenseAdapter(TripExpensesActivity.this,this, expenses);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(TripExpensesActivity.this, 3));
    }

    /**
     * Code taken from https://github.com/stevdza-san/SQLite_Android-Complete_Tutorial/blob/master/app/src/main/java/com/jovanovic/stefan/sqlitetutorial/MainActivity.java
     */
    void storeDataInArrays() {
        expenses = databaseHandler.getTripExpenses(getIntent().getStringExtra("trip_id"));
        if (expenses.size() == 0) {
            empty_imageview.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);
        } else {
            empty_imageview.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
