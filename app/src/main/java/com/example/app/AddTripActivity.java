package com.example.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import java.time.LocalDate;

public class AddTripActivity extends AppCompatActivity {

    EditText name, destination, date, description, duration, aim, status;
    String risk_assessment;
    Button save_button;
    private boolean allConditionChecked = true;

    private final String[] riskAssessmentDropdown = { "No", "Yes" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        name = findViewById(R.id.add_name_trip_column);
        destination = findViewById(R.id.add_destination_trip_column);
        date = findViewById(R.id.add_date_trip_column);

        Spinner risk_assessment_spinner = (Spinner) findViewById(R.id.add_risk_assessment_trip_column);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, riskAssessmentDropdown);
        risk_assessment_spinner.setAdapter(dataAdapter);

        description = findViewById(R.id.add_description_trip_column);
        duration = findViewById(R.id.add_duration_trip_column);
        aim = findViewById(R.id.add_aim_trip_column);
        status = findViewById(R.id.add_status_trip_column);

        checkEditTextErrors(name);
        checkEditTextErrors(destination);
        checkEditTextErrors(date);
        checkEditTextErrors(duration);
        checkEditTextErrors(status);

        save_button = findViewById(R.id.add_save_trip_db_button);
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isTextEmpty(name);
                isTextEmpty(destination);
                isTextEmpty(date);
                isTextEmpty(duration);
                isTextEmpty(status);

                if (allConditionChecked) {
                    risk_assessment = risk_assessment_spinner.getSelectedItem().toString();
                    confirmTripDetails(risk_assessment);
                } else {
                    allConditionChecked = true;
                }
            }
        });
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

    /**
     * Code taken from https://stackoverflow.com/questions/18225365/show-error-on-the-tip-of-the-edit-text-android
     */
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
                }
            }
        });
    }

    public void confirmTripDetails(String risk_assessment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddTripActivity.this);
        builder.setTitle("Do you confirm these details?");

        String message = String.format("Name: " + name.getText().toString().trim() + "\n" +
            "Destination: " + destination.getText().toString().trim() + "\n" +
             "Date: " + date.getText().toString().trim() + "\n" +
             "Risk Assessment: " + risk_assessment + "\n" +
             "Description: " + description.getText().toString().trim() + "\n" +
             "Duration: " + duration.getText().toString().trim() + "\n" +
             "Aim: " + aim.getText().toString().trim() + "\n" +
             "Status: " + status.getText().toString().trim() + "\n");

        builder.setMessage(message);
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SqliteDatabaseHandler databaseHandler = new SqliteDatabaseHandler(AddTripActivity.this);

                databaseHandler.insertTrip(
                        name.getText().toString().trim(), destination.getText().toString().trim(), date.getText().toString().trim(),
                        risk_assessment, description.getText().toString().trim(),
                        duration.getText().toString().trim(), aim.getText().toString().trim(), status.getText().toString().trim()
                );
                Intent intent = new Intent(AddTripActivity.this, MainTripActivity.class);
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

    public void showDatePickerDialog(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.setCancelable(false);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void updateDate(LocalDate dob) {
        TextView dobText = (TextView)findViewById(R.id.add_date_trip_column);
        dobText.setText(dob.toString());
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            LocalDate d = LocalDate.now();
            int year = d.getYear();
            int month = d.getMonthValue();
            int day = d.getDayOfMonth();

            DatePickerDialog picker = new DatePickerDialog(getActivity(), this, year, --month, day);
            picker.getDatePicker().setMinDate(System.currentTimeMillis());
            return picker;
        }

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            LocalDate dob = LocalDate.of(year, ++month, day);
            ((AddTripActivity)getActivity()).updateDate(dob);
        }
    }
}
