package com.example.project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;

public class MainTripActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton add_button;
    TextView instructions_trip;

    SqliteDatabaseHandler databaseHandler;
    List<Trip> trips;
    List<JsonCloudModel> jsonCloudData;
    RecyclerViewTripAdapter customAdapter;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_main);

        recyclerView = findViewById(R.id.trip_recycler_view);
        add_button = findViewById(R.id.trip_add_main_button);
        instructions_trip = findViewById(R.id.instructions_trip);

        add_button.setOnClickListener(view -> {
            Intent intent = new Intent(MainTripActivity.this, AddTripActivity.class);
            startActivity(intent);
        });
        databaseHandler = new SqliteDatabaseHandler(MainTripActivity.this);
        trips = new ArrayList<>();
        jsonCloudData = new ArrayList<>();
        getTripsData();

        customAdapter = new RecyclerViewTripAdapter(MainTripActivity.this,this, trips);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(MainTripActivity.this, 3));

        // https://medium.com/@makkenasrinivasarao1/bottom-navigation-in-android-application-with-activities-material-design-7a056b8cf38
        BottomNavigationView bottomNavigation = findViewById(R.id.navigation_bottom);
        bottomNavigation.setSelectedItemId(R.id.home_navigation);
        bottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home_navigation:
                    startActivity(new Intent(getApplicationContext(), MainTripActivity.class));
                    break;
                case R.id.search_navigation:
                    startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                    break;
                case R.id.settings_navigation:
                    startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                    break;
            }
            return true;
        });
    }

    private void getTripsData() {
        trips = databaseHandler.getAllTrips();
        if (trips.size() == 0) {
            instructions_trip.setVisibility(View.VISIBLE);
        } else {
            instructions_trip.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.upload_data_cloud:
                uploadDataWebCloud();
                return true;
            case R.id.delete_all_data:
                confirmDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // https://stackoverflow.com/questions/36833798/sending-json-object-via-http-post-method-in-android
    // https://stackoverflow.com/questions/2938502/sending-post-data-in-android
    public void uploadDataWebCloud() {
        // https://www.educative.io/edpresso/how-to-fix-androidosnetworkonmainthreadexception-error
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        jsonCloudData = databaseHandler.getJsonCloudData();

        try {
            JSONObject jsonData = new JSONObject();
            jsonData.put("userId", "001095988");

            JSONArray expenseArray = new JSONArray();
            for (int index = 0; index < jsonCloudData.size(); index++) {
                JSONObject expense = new JSONObject();

                expense.put("name", jsonCloudData.get(index).getTripName());
                expense.put("expense_type", jsonCloudData.get(index).getType());
                expense.put("expense_amount", jsonCloudData.get(index).getAmount());
                expense.put("expense_time", jsonCloudData.get(index).getTime());
                expense.put("expense_additional_comments", jsonCloudData.get(index).getAdditionalComments());

                expenseArray.put(expense);
            }
            jsonData.put("detailList", expenseArray);



            URL url = new URL("https://stuiis.cms.gre.ac.uk/COMP1424CoreWS/comp1424cw");
            HttpsURLConnection httpsConnection = (HttpsURLConnection)url.openConnection();
            httpsConnection.setDoOutput(true);
            httpsConnection.setRequestMethod("POST");
            httpsConnection.setRequestProperty("Content-Type", "application/json;");
            httpsConnection.connect();

            OutputStream out = new BufferedOutputStream(httpsConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
            writer.write(jsonData.toString());
            writer.flush();
            writer.close();
            out.close();

            int response_code = httpsConnection.getResponseCode();
            if (response_code >= HttpsURLConnection.HTTP_OK && response_code <= HttpsURLConnection.HTTP_PARTIAL) {
                JSONObject jsonObject = new JSONObject(httpsConnection.getResponseMessage());

                // FOR TESTING PURPOSE
                /*

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("uploadResponseCode", "SUCCESS");
                    jsonObject.put("userid", "wm123");
                    jsonObject.put("number", 2);
                    jsonObject.put("names", "Android Conference, Client Meeting");
                    jsonObject.put("message", "successful upload – all done!");
                */

                displayResponseMessageDialog(jsonObject);
            } else {
                Toast.makeText(this, "Error: Something went wrong with the request process.", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException | IOException e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void displayResponseMessageDialog(JSONObject message) throws JSONException {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainTripActivity.this);
        builder.setCancelable(false);
        builder.setTitle("Upload Data Response");
        String formatMessage = "Upload Response Code: " + message.getString("uploadResponseCode") + "\n" +
                "UserId: " + message.getString("userId") + "\n" +
                "Number: " + message.getInt("number") + "\n" +
                "Names: " + message.getString("names") + "\n" +
                "Message: " + message.getString("message") + "\n";
        builder.setMessage(formatMessage);
        builder.setNegativeButton("Close Dialog", (dialog, which) -> dialog.cancel());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // https://www.geeksforgeeks.org/android-alert-dialog-box-and-how-to-create-it/
    private void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainTripActivity.this);
        builder.setTitle("Delete Database Data");
        builder.setCancelable(false);

        if (trips.size() > 0) {
            builder.setMessage("Are you sure you want to delete all the data from database?");

            builder.setPositiveButton("Yes", (dialog, which) -> {
                databaseHandler.deleteAllData();
                Intent intent = new Intent(MainTripActivity.this, MainTripActivity.class);
                startActivity(intent);
                finish();
            });
            builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
        } else {
            builder.setMessage("There are not data stored in the database at this moment.");
            builder.setNegativeButton("Close Dialog", (dialog, which) -> dialog.cancel());
        }

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
