package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {

    EditText name, destination, date, risk_assessment, description, duration, aim, status;
    Button save_button;

    private boolean allConditionChecked = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        name = findViewById(R.id.name_column);
        destination = findViewById(R.id.destination_column);
        date = findViewById(R.id.date_column);
        risk_assessment = findViewById(R.id.risk_assessment_column);
        description = findViewById(R.id.description_column);
        duration = findViewById(R.id.duration_column);
        aim = findViewById(R.id.aim_column);
        status = findViewById(R.id.status_column);

        checkEditTextErrors(name);
        checkEditTextErrors(destination);
        checkEditTextErrors(date);
        checkEditTextErrors(risk_assessment);
        checkEditTextErrors(description);
        checkEditTextErrors(duration);
        checkEditTextErrors(aim);
        checkEditTextErrors(status);

/*
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (name.getText().toString().length() <= 3) {
                    name.setError("The value must have at least 4 characters.");
                } else {
                    name.setError(null);
                }
            }
        });
*/
        save_button = findViewById(R.id.save_trip_db_button);
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEditTextEmpty(name);
                isEditTextEmpty(destination);
                isEditTextEmpty(date);
                isEditTextEmpty(risk_assessment);
                isEditTextEmpty(description);
                isEditTextEmpty(duration);
                isEditTextEmpty(aim);
                isEditTextEmpty(status);

                if (allConditionChecked) {
                    SqliteDatabaseHandler databaseHandler = new SqliteDatabaseHandler(AddActivity.this);
                    String beniamin = name.getText().toString().trim();

                    databaseHandler.insertTrip(
                        name.getText().toString().trim(), destination.getText().toString().trim(), date.getText().toString().trim(),
                        risk_assessment.getText().toString().trim(), description.getText().toString().trim(),
                        duration.getText().toString().trim(), aim.getText().toString().trim(), status.getText().toString().trim()
                    );
                } else {
                    allConditionChecked = true;
                }
            }
        });
    }

    public void isEditTextEmpty(EditText textName) {
        if (textName.getText().toString().length() == 0) {
            textName.setError("This field is required.");
            allConditionChecked = false;
        } else if (textName.getText().toString().length() <= 2) {
            textName.setError("The value must have at least 3 characters.");
            allConditionChecked = false;
        }
    }

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
}
