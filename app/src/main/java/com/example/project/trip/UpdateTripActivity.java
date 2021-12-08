package com.example.project.trip;

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
import android.widget.Toast;
import com.example.project.MainTripActivity;
import com.example.project.R;
import com.example.project.database.SqliteDatabaseHandler;
import java.util.Arrays;

public class UpdateTripActivity extends AppCompatActivity {
    DatePickerDialog picker;
    EditText name, destination, date, description, duration, aim;
    Spinner risk_assessment_spinner, status_spinner;
    String risk_assessment, status;
    Button update_button;
    private boolean allConditionChecked = true;

    private final String[] riskAssessmentDropdown = { "No", "Yes" };
    private final String[] statusDropdown = { "Started", "OnGoing", "Finished" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_trip);

        name = findViewById(R.id.update_name_trip_column);
        destination = findViewById(R.id.update_destination_trip_column);

        date = findViewById(R.id.update_date_trip_column);
        date.setOnClickListener(v -> showUpdateDatePicker());

        description = findViewById(R.id.update_description_trip_column);
        duration = findViewById(R.id.update_duration_trip_column);
        aim = findViewById(R.id.update_aim_trip_column);

        risk_assessment_spinner = (Spinner) findViewById(R.id.update_risk_assessment_trip_column);
        ArrayAdapter<String> dataRiskAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, riskAssessmentDropdown);
        risk_assessment_spinner.setAdapter(dataRiskAdapter);

        status_spinner = (Spinner) findViewById(R.id.update_status_trip_column);
        ArrayAdapter<String> dataStatusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, statusDropdown);
        status_spinner.setAdapter(dataStatusAdapter);

        populateUpdateTripText();

        checkEditTextErrors(name);
        checkEditTextErrors(destination);
        checkEditTextErrors(date);
        checkEditTextErrors(duration);

        update_button = findViewById(R.id.update_save_trip_db_button);
        update_button.setOnClickListener(view -> {
            isTextEmpty(name);
            isTextEmpty(destination);
            isTextEmpty(date);
            isTextEmpty(duration);

            if (allConditionChecked) {
                risk_assessment = risk_assessment_spinner.getSelectedItem().toString();
                status = status_spinner.getSelectedItem().toString();
                saveIntoDatabase(risk_assessment, status);
            } else {
                allConditionChecked = true;
            }
        });
    }

    public void saveIntoDatabase(String risk_assessment, String status) {
        SqliteDatabaseHandler databaseHandler = new SqliteDatabaseHandler(UpdateTripActivity.this);
        databaseHandler.updateTrip(
                getIntent().getStringExtra("id"),
                name.getText().toString().trim(), destination.getText().toString().trim(), date.getText().toString().trim(),
                risk_assessment, description.getText().toString().trim(),
                duration.getText().toString().trim(), aim.getText().toString().trim(), status
        );
        Intent intent = new Intent(UpdateTripActivity.this, MainTripActivity.class);
        startActivity(intent);
        finish();
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
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                if (textName.getText().toString().length() <= 2) {
                    textName.setError("The value must have at least 3 characters.");
                }
            }
        });
    }

    void populateUpdateTripText() {
        if (getIntent().hasExtra("id") && getIntent().hasExtra("name") &&
                getIntent().hasExtra("destination") && getIntent().hasExtra("date") &&
                getIntent().hasExtra("risk_assessment") && getIntent().hasExtra("duration") &&
                getIntent().hasExtra("status")
        ) {
            name.setText(getIntent().getStringExtra("name"));
            destination.setText(getIntent().getStringExtra("destination"));
            date.setText(getIntent().getStringExtra("date"));
            description.setText(getIntent().getStringExtra("description"));
            duration.setText(getIntent().getStringExtra("duration"));
            aim.setText(getIntent().getStringExtra("aim"));

            int risk_dropdown_position = Arrays.asList(riskAssessmentDropdown).indexOf(getIntent().getStringExtra("risk_assessment"));
            int status_dropdown_position = Arrays.asList(statusDropdown).indexOf(getIntent().getStringExtra("status"));

            risk_assessment_spinner.setSelection(risk_dropdown_position);
            status_spinner.setSelection(status_dropdown_position);
        } else {
            Toast.makeText(UpdateTripActivity.this, "No data was received by the Update Activity", Toast.LENGTH_LONG).show();
        }
    }

    // https://www.tutlane.com/tutorial/android/android-datepicker-with-examples
    @SuppressLint("SetTextI18n")
    void showUpdateDatePicker() {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);

        picker = new DatePickerDialog(UpdateTripActivity.this,
                (view, year1, monthOfYear, dayOfMonth) -> date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1), year, month, day);
        picker.getDatePicker().setMinDate(System.currentTimeMillis());
        picker.show();
    }
}
