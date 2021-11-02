package com.example.AndroidResourcesgementapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayDetails extends AppCompatActivity {
    public static final String NAME = "name";
    public static final String PHONE = "phone";
    public static final String EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_details);

        TextView display = (TextView) findViewById(R.id.displayDetails);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            display.setText("");
            String name = extras.getString(NAME);
            String phone = extras.getString(PHONE);
            String email = extras.getString(EMAIL);
            if (name != null) {
                display.append("Name Entered: " + name);
            }
            if (phone != null) {
                display.append("Phone Entered: " + phone);
            }
            if (email != null) {
                display.append("Email Entered: " + email);
            }
        }
    }
}