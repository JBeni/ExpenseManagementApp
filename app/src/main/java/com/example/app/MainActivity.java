package com.example.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.app.ui.dashboard.DashboardFragment;
import com.example.app.ui.notifications.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.app.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements AddEditTripFragment.OnSaveClicked {

    private ActivityMainBinding binding;

    Fragment fragment = null;
    FragmentTransaction fragmentTransaction;

    private boolean mTwoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications).build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        fragment = new HomeFragment();
        switchFragment(fragment);


        /**
         * Help from https://stackoverflow.com/questions/67641594/bottomnavigation-view-onnavigationitemselectedlistener-is-deprecated
         */
        navView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_dashboard:
                        fragment = new DashboardFragment();
                        switchFragment(fragment);
                        break;
                    case R.id.navigation_notifications:
                        fragment = new NotificationsFragment();
                        switchFragment(fragment);
                        break;
                    default:
                        fragment = new DataFragment();
                        switchFragment(fragment);
                }
                return true;
            }
        });
    }




/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        MenuItem generate = menu.findItem(R.id.add_trip_button);
        generate.setVisible(true);
        return true;
    }
*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_trip_button:
                addEditTripRequest(null);
                System.out.println("HOME FRAGMENT ADD BUTTON");
                break;
            case R.id.edit_trip_button:
                //addEditTripRequest(trip);
                System.out.println("HOME FRAGMENT ADD BUTTON");
                break;
            case R.id.delete_trip_button:
                // startActivity(new Intent(this, DurationsReport.class));
                System.out.println("DASHBOARD FRAGMENT DELETE BUTTON");
                break;
/*            case R.id.menumain_settings:
                break;
            case R.id.menumain_showAbout:
                showAboutDialog();
                break;
            case R.id.menumain_generate:
                TestData.generateTestData(getContentResolver());
                break;
            case android.R.id.home:
                Log.d(TAG, "onOptionsItemSelected: home button pressed");
                AddEditActivityFragment fragment = (AddEditActivityFragment)
                        getSupportFragmentManager().findFragmentById(R.id.task_details_container);
                if(fragment.canClose()) {
                    return super.onOptionsItemSelected(item);
                } else {
                    showConfirmationDialog(DIALOG_ID_CANCEL_EDIT_UP);
                    return true;  // indicate we are handling this
                }
                */
        }

        return super.onOptionsItemSelected(item);
    }

    public void addEditTripRequest(Trip trip) {
        AddEditTripFragment fragment = new AddEditTripFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(Trip.class.getSimpleName(), trip);
        fragment.setArguments(arguments);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.navigation_home, fragment)
                .commit();
    }



    /**
     * Taken from https://stackoverflow.com/questions/43104485/how-to-change-fragment-with-the-bottom-navigation-activity
     */
    private void switchFragment(Fragment fragment) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }


    @Override
    public void onSaveClicked() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.navigation_home);
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commit();
        }

        View addEditLayout = findViewById(R.id.navigation_home);
        View mainFragment = findViewById(R.id.navigation_home);

        if(!mTwoPane) {
            // We've just removed the editing fragment, so hide the frame
            addEditLayout.setVisibility(View.GONE);

            // and make sure the MainActivityFragment is visible.
            mainFragment.setVisibility(View.VISIBLE);
        }
    }
}
