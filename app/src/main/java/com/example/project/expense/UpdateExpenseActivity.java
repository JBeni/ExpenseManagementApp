package com.example.project.expense;

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
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.project.MainTripExpensesActivity;
import com.example.project.R;
import com.example.project.database.SqliteDatabaseHandler;

public class UpdateExpenseActivity extends AppCompatActivity {
    TimePickerDialog picker;
    EditText type, amount, time, additional_comments;
    Button update_save_button;
    private boolean allConditionChecked = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_update_activity);

        type = findViewById(R.id.update_type_expense_column);
        amount = findViewById(R.id.update_amount_expense_column);
        additional_comments = findViewById(R.id.update_additional_comments_expense_column);

        time = findViewById(R.id.update_time_expense_column);
        time.setFocusable(false);
        time.setInputType(InputType.TYPE_NULL);
        updateTimePicker(time);

        populateUpdateExpenseText();

        checkTextErrors(type);
        checkTextErrors(amount);
        checkTextErrors(time);
        checkTextErrors(additional_comments);

        update_save_button = findViewById(R.id.update_save_expense_db_button);
        update_save_button.setOnClickListener(view -> {
            isTextEmpty(type);
            isTextEmpty(amount);
            isTextEmpty(time);
            isTextEmpty(additional_comments);

            if (allConditionChecked) {
                saveIntoDatabase();
            } else {
                allConditionChecked = true;
            }
        });
    }

    public void saveIntoDatabase() {
        SqliteDatabaseHandler databaseHandler = new SqliteDatabaseHandler(UpdateExpenseActivity.this);
        databaseHandler.updateExpense(
                getIntent().getStringExtra("id"),
                type.getText().toString().trim(), amount.getText().toString().trim(),
                time.getText().toString().trim(), additional_comments.getText().toString().trim()
        );
        Intent intent = new Intent(UpdateExpenseActivity.this, MainTripExpensesActivity.class);
        startActivity(intent);
        finish();
    }

    void populateUpdateExpenseText() {
        if (getIntent().hasExtra("id") && getIntent().hasExtra("type") &&
                getIntent().hasExtra("amount") && getIntent().hasExtra("time") && getIntent().hasExtra("trip_id")
        ) {
            type.setText(getIntent().getStringExtra("type"));
            amount.setText(getIntent().getStringExtra("amount"));
            time.setText(getIntent().getStringExtra("time"));
            additional_comments.setText(getIntent().getStringExtra("additional_comments"));
        } else {
            Toast.makeText(UpdateExpenseActivity.this, "No data was received by the Update Activity", Toast.LENGTH_LONG).show();
        }
    }

    // https://www.tutlane.com/tutorial/android/android-timepicker-with-examples
    @SuppressLint("SetTextI18n")
    public void updateTimePicker(EditText eText) {
        eText.setOnClickListener(v -> {
            final Calendar cldr = Calendar.getInstance();
            int hour = cldr.get(Calendar.HOUR_OF_DAY);
            int minutes = cldr.get(Calendar.MINUTE);

            picker = new TimePickerDialog(UpdateExpenseActivity.this,
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
