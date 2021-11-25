package com.example.project;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class AddExpenseActivity extends AppCompatActivity {
    TimePickerDialog picker;
    EditText type, amount, time, additional_comments;
    Button save_button;
    private boolean allConditionChecked = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        type = findViewById(R.id.add_type_expense_column);
        amount = findViewById(R.id.add_amount_expense_column);
        additional_comments = findViewById(R.id.add_additional_comments_expense_column);

        time = findViewById(R.id.add_time_expense_column);
        time.setFocusable(false);
        time.setInputType(InputType.TYPE_NULL);
        addTimePicker(time);

        checkTextErrors(type);
        checkTextErrors(amount);
        checkTextErrors(time);
        checkTextErrors(additional_comments);

        save_button = findViewById(R.id.add_save_expense_db_button);
        save_button.setOnClickListener(view -> {
            isTextEmpty(type);
            isTextEmpty(amount);
            isTextEmpty(time);
            isTextEmpty(additional_comments);

            if (allConditionChecked) {
                SqliteDatabaseHandler databaseHandler = new SqliteDatabaseHandler(AddExpenseActivity.this);

                SharedTripViewModel sharedTripView = (SharedTripViewModel) getApplicationContext();
                String trip_id = sharedTripView.getSharedTripId();

                databaseHandler.insertExpense(
                    type.getText().toString().trim(), amount.getText().toString().trim(), time.getText().toString().trim(),
                        additional_comments.getText().toString().trim(), String.valueOf(trip_id)
                );
                Intent intent = new Intent(AddExpenseActivity.this, MainTripExpensesActivity.class);
                startActivity(intent);
                finish();
            } else {
                allConditionChecked = true;
            }
        });
    }

    // https://www.tutlane.com/tutorial/android/android-timepicker-with-examples
    @SuppressLint("SetTextI18n")
    public void addTimePicker(EditText eText) {
        eText.setOnClickListener(v -> {
            final Calendar cldr = Calendar.getInstance();
            int hour = cldr.get(Calendar.HOUR_OF_DAY);
            int minutes = cldr.get(Calendar.MINUTE);

            picker = new TimePickerDialog(AddExpenseActivity.this,
                    (tp, sHour, sMinute) -> eText.setText(sHour + ":" + sMinute), hour, minutes, true);
            picker.setCancelable(false);
            picker.show();
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

    // https://stackoverflow.com/questions/18225365/show-error-on-the-tip-of-the-edit-text-android
    public void checkTextErrors(EditText textName) {
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
