package com.example.project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AddTripActivity extends AppCompatActivity {
    DatePickerDialog picker;
    EditText name, destination, date, description, duration, aim;
    String risk_assessment, status;
    Button save_button;
    private boolean allConditionChecked = true;

    private final String[] riskAssessmentDropdown = { "No", "Yes" };
    private final String[] statusDropdown = { "Started", "OnGoing", "Finished" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        name = findViewById(R.id.add_name_trip_column);
        destination = findViewById(R.id.add_destination_trip_column);
        date = findViewById(R.id.add_date_trip_column);

        date.setOnClickListener(v -> showAddDatePicker());

        Spinner risk_assessment_spinner = findViewById(R.id.add_risk_assessment_trip_column);
        ArrayAdapter<String> dataRiskAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, riskAssessmentDropdown);
        risk_assessment_spinner.setAdapter(dataRiskAdapter);

        Spinner status_spinner = findViewById(R.id.add_status_trip_column);
        ArrayAdapter<String> dataStatusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, statusDropdown);
        status_spinner.setAdapter(dataStatusAdapter);

        description = findViewById(R.id.add_description_trip_column);
        duration = findViewById(R.id.add_duration_trip_column);
        aim = findViewById(R.id.add_aim_trip_column);

        checkEditTextErrors(name);
        checkEditTextErrors(destination);
        checkEditTextErrors(date);
        checkEditTextErrors(duration);

        save_button = findViewById(R.id.add_save_trip_db_button);
        save_button.setOnClickListener(view -> {
            isTextEmpty(name);
            isTextEmpty(destination);
            isTextEmpty(date);
            isTextEmpty(duration);

            if (allConditionChecked) {
                risk_assessment = risk_assessment_spinner.getSelectedItem().toString();
                status = status_spinner.getSelectedItem().toString();
                confirmTripDetails(risk_assessment, status);
            } else {
                allConditionChecked = true;
            }
        });
    }

    // https://www.tutlane.com/tutorial/android/android-datepicker-with-examples
    @SuppressLint("SetTextI18n")
    void showAddDatePicker() {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);

        picker = new DatePickerDialog(AddTripActivity.this,
                (view, year1, monthOfYear, dayOfMonth) -> date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1), year, month, day);
        picker.setCancelable(false);
        picker.getDatePicker().setMinDate(System.currentTimeMillis());
        picker.show();
    }

    public void isTextEmpty(EditText textName) {
        if (textName.getText().toString().length() == 0) {
            textName.setError("This field is required.");
            allConditionChecked = false;
        } else if (textName.getText().toString().length() <= 2) {
            textName.setError("The value must have at least 3 characters.");
            allConditionChecked = false;
        }
    }

    // https://stackoverflow.com/questions/18225365/show-error-on-the-tip-of-the-edit-text-android
    public void checkEditTextErrors(EditText textName) {
        textName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (textName.getText().toString().length() <= 2) {
                    textName.setError("The value must have at least 3 characters.");
                } else {
                    textName.setError(null);
                }
            }
        });
    }

    public void confirmTripDetails(String risk_assessment, String status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddTripActivity.this);
        builder.setTitle("Do you confirm these details?");

        String message = "Name: " + name.getText().toString().trim() + "\n" +
                "Destination: " + destination.getText().toString().trim() + "\n" +
                "Date: " + date.getText().toString().trim() + "\n" +
                "Risk Assessment: " + risk_assessment + "\n" +
                "Description: " + description.getText().toString().trim() + "\n" +
                "Duration: " + duration.getText().toString().trim() + "\n" +
                "Aim: " + aim.getText().toString().trim() + "\n" +
                "Status: " + status + "\n";

        builder.setMessage(message);
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", (dialog, which) -> {
            SqliteDatabaseHandler databaseHandler = new SqliteDatabaseHandler(AddTripActivity.this);

            databaseHandler.insertTrip(
                    name.getText().toString().trim(), destination.getText().toString().trim(), date.getText().toString().trim(),
                    risk_assessment, description.getText().toString().trim(),
                    duration.getText().toString().trim(), aim.getText().toString().trim(), status
            );
            Intent intent = new Intent(AddTripActivity.this, MainTripActivity.class);
            startActivity(intent);
            finish();
        });

        builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
