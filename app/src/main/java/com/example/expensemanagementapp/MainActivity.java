package com.example.expensemanagementapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;

public class MainActivity extends AppCompatActivity {

    private EditText textEditName;
    private EditText textEditPhone;
    private EditText textEditEmail;

    private final String[] countries = { "England", "Northern Ireland", "Scotland", "Wales" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner sp = (Spinner) findViewById(R.id.workStatusDropdown);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(
            this, android.R.layout.simple_spinner_item, countries
        );
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // sp.setAdapter(dataAdapter);

        textEditName = (EditText) findViewById(R.id.textEditName);
        textEditPhone = (EditText) findViewById(R.id.textEditPhone);
        textEditEmail = (EditText) findViewById(R.id.textEditEmail);

        Button save = (Button)findViewById(R.id.saveButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox cb = (CheckBox)findViewById(R.id.agreeTermsConditionsOption);
                if (!cb.isChecked()) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                        "You must agree to the terms", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                getInputs();
            }
        });
    }

    public void handleButtonClick(View view)
    {
        String strName = textEditName.getText().toString();
        String strPhone = textEditPhone.getText().toString();
        String strEmail = textEditEmail.getText().toString();

        Intent intent = new Intent(this, DisplayDetails.class);
        intent.putExtra(DisplayDetails.NAME, strName);
        intent.putExtra(DisplayDetails.PHONE, strPhone);
        intent.putExtra(DisplayDetails.EMAIL, strEmail);

        startActivity(intent);
    }

    public void showDatePickerDialog(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void updateDOB(LocalDate dob) {
        TextView dobText = (TextView)findViewById(R.id.datePickerDialog);
        dobText.setText(dob.toString());
    }

    private void getInputs() {
        EditText nameInput = (EditText)findViewById(R.id.textEditName);
        EditText emailInput = (EditText)findViewById(R.id.textEditEmail);
        EditText phoneInput = (EditText)findViewById(R.id.textEditPhone);
        RadioGroup group = (RadioGroup)findViewById(R.id.radioGroup);
        RadioButton radioButtonInput = (RadioButton)findViewById(group.getCheckedRadioButtonId());

        String strName = nameInput.getText().toString(),
                strPhone = phoneInput.getText().toString(),
                strEmail = emailInput.getText().toString(),
                strWork = radioButtonInput.getText().toString();
        displayNextAlert(strName, strPhone, strEmail, strWork);
    }

    private void displayNextAlert(String strName, String strPhone, String strEmail, String strWork) {
        new AlertDialog.Builder(this).setTitle("Details entered").setMessage("Details entered:\n" + strName + "\n" + strPhone + "\n"
                + strEmail + "\n" + strWork).setNeutralButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) { } }).show();
    }


    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            LocalDate d = LocalDate.now();
            int year = d.getYear();
            int month = d.getMonthValue();
            int day = d.getDayOfMonth();

            return new DatePickerDialog(getActivity(), this, year, --month, day);
        }

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            LocalDate dob = LocalDate.of(year, ++month, day);
            ((MainActivity)getActivity()).updateDOB(dob);
        }
    }
}
