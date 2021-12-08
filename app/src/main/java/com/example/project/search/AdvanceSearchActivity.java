package com.example.project.search;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.example.project.R;

public class AdvanceSearchActivity extends AppCompatActivity {
    DatePickerDialog picker;
    EditText name, destination, date;
    Button search_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_advance_activity);

        name = findViewById(R.id.advance_search_trip_name);
        destination = findViewById(R.id.advance_search_trip_destination);
        date = findViewById(R.id.advance_search_trip_date);
        date.setOnClickListener(v -> showAddDatePicker());

        search_button = findViewById(R.id.advance_search_trip_button);
        search_button.setOnClickListener(view -> {
            Intent advance_search_data = new Intent();
            advance_search_data.putExtra("advance_search_name", name.getText().toString());
            advance_search_data.putExtra("advance_search_destination", destination.getText().toString());
            advance_search_data.putExtra("advance_search_date", date.getText().toString());
            setResult(RESULT_OK, advance_search_data);
            finish();
        });
    }

    // https://www.tutlane.com/tutorial/android/android-datepicker-with-examples
    @SuppressLint("SetTextI18n")
    void showAddDatePicker() {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);

        picker = new DatePickerDialog(AdvanceSearchActivity.this,
                (view, year1, monthOfYear, dayOfMonth) -> date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1), year, month, day);
        picker.setCancelable(false);
        picker.show();
    }
}
