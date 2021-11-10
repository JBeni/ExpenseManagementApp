package com.example.app;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ViewExpenseActivity extends AppCompatActivity {
    TextView type, amount, time, additional_comments;

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
    }
}
