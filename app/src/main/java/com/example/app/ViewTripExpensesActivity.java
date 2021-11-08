package com.example.app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ViewTripExpensesActivity extends AppCompatActivity {

    FloatingActionButton update_button, delete_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trip);

/*
        update_button = findViewById(R.id.edit_expense_button);
        delete_button = findViewById(R.id.delete_expense_button);

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //And only then we call this
                //SqliteDatabaseHandler myDB = new SqliteDatabaseHandler(UpdateTripActivity.this);
                //title = title_input.getText().toString().trim();
                //author = author_input.getText().toString().trim();
                //pages = "3";
                //myDB.updateData(id, title, author, pages);
            }
        });
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //confirmDialog();
            }
        });
*/
    }

}
