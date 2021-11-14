package com.example.project;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainTripActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton add_button;
    TextView instructions_trip;

    SqliteDatabaseHandler databaseHandler;
    List<Trip> trips;
    List<JsonCloudModel> jsonCloudData;
    RecyclerViewTripAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_main);

        recyclerView = findViewById(R.id.trip_recycler_view);
        add_button = findViewById(R.id.trip_add_main_button);
        instructions_trip = findViewById(R.id.instructions_trip);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainTripActivity.this, AddTripActivity.class);
                startActivity(intent);
            }
        });
        databaseHandler = new SqliteDatabaseHandler(MainTripActivity.this);
        trips = new ArrayList<Trip>();
        jsonCloudData = new ArrayList<JsonCloudModel>();
        getTripsData();

        customAdapter = new RecyclerViewTripAdapter(MainTripActivity.this,this, trips);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(MainTripActivity.this, 3));

        /**
         * https://medium.com/@makkenasrinivasarao1/bottom-navigation-in-android-application-with-activities-material-design-7a056b8cf38
         ***/
        BottomNavigationView bottomNavigation = findViewById(R.id.navigation_bottom);
        bottomNavigation.setSelectedItemId(R.id.home_navigation);
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
            }
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

    /**
     * https://stackoverflow.com/questions/36833798/sending-json-object-via-http-post-method-in-android
     */
    public void uploadDataWebCloud() {
        jsonCloudData = databaseHandler.getJsonCloudData();

        try {
            String jsonPayload;
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
            jsonPayload = jsonData.toString();

            URL url = new URL("https://stuiis.cms.gre.ac.uk/COMP1424CoreWS/comp1424cw");
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.connect();

            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            wr.writeBytes(jsonPayload.toString());
            wr.flush();
            wr.close();

            // DISPLAY THE RESPONSE MESSAGE TO USER

        } catch (JSONException | MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * https://www.geeksforgeeks.org/android-alert-dialog-box-and-how-to-create-it/
     */
    private void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainTripActivity.this);
        builder.setTitle("Delete Database Data");
        builder.setMessage("Are you sure you want to delete all the data from database?");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseHandler.deleteAllData();
                Intent intent = new Intent(MainTripActivity.this, MainTripActivity.class);
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
}
