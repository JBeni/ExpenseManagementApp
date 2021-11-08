package com.example.app;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class AddExpenseActivity extends AppCompatActivity {

    EditText type, amount, time, additional_comments;
    Button save_button;
    private boolean allConditionChecked = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        type = findViewById(R.id.type_expense_column);
        amount = findViewById(R.id.amount_expense_column);
        time = findViewById(R.id.time_expense_column);
        additional_comments = findViewById(R.id.additional_comments_expense_column);

        checkEditTextErrors(type);
        checkEditTextErrors(amount);
        checkEditTextErrors(time);
        checkEditTextErrors(additional_comments);

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

        save_button = findViewById(R.id.save_expense_db_button_text);
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEditTextEmpty(type);
                isEditTextEmpty(amount);
                isEditTextEmpty(time);
                isEditTextEmpty(additional_comments);

                if (allConditionChecked) {
                    SqliteDatabaseHandler databaseHandler = new SqliteDatabaseHandler(AddExpenseActivity.this);

                    databaseHandler.insertExpense(
                        type.getText().toString().trim(), amount.getText().toString().trim(), time.getText().toString().trim(),
                            additional_comments.getText().toString().trim(), Long.parseLong(getIntent().getStringExtra("trip_id"))
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
