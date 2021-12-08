package com.example.project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ViewExpenseActivity extends AppCompatActivity {
    TextView type, amount, time, additional_comments;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_expense);

        type = findViewById(R.id.view_type_expense_column);
        amount = findViewById(R.id.view_amount_expense_column);
        time = findViewById(R.id.view_time_expense_column);
        additional_comments = findViewById(R.id.view_additional_comments_expense_column);

        if (getIntent().hasExtra("id") && getIntent().hasExtra("type") &&
                getIntent().hasExtra("amount") && getIntent().hasExtra("time")
        ) {
            type.setText(getIntent().getStringExtra("type"));
            amount.setText(getIntent().getStringExtra("amount"));
            time.setText(getIntent().getStringExtra("time"));

            additional_comments.setText(
                    getIntent().getStringExtra("additional_comments").length() == 0
                            ? "Empty field"
                            : getIntent().getStringExtra("additional_comments")
            );
        }

        // https://medium.com/@makkenasrinivasarao1/bottom-navigation-in-android-application-with-activities-material-design-7a056b8cf38
        BottomNavigationView bottomNavigation = findViewById(R.id.navigation_bottom);
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
}
