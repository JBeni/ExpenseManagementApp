package com.example.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class AddEditTripFragment extends Fragment {
    private static final String TAG = "AddEditActivityFragment";

    private enum FragmentEditMode { EDIT, ADD }
    private FragmentEditMode mMode;

    private EditText nameTextView;
    private EditText destinationTextView;
    private EditText dateTextView;
    private EditText riskAssessmentTextView;
    private EditText descriptionTextView;
    private EditText durationTextView;
    private EditText aimTextView;
    private EditText statusTextView;

    private EditText mDescriptionTextView;
    private EditText mSortOrderTextView;

    private OnSaveClicked mSaveListener = null;

    interface OnSaveClicked {
        void onSaveClicked();
    }

    public AddEditTripFragment() {
        Log.d(TAG, "AddEditActivityFragment: constructor called");
    }

    public boolean canClose() {
        return false;
    }

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach: starts");
        super.onAttach(context);

        // Activities containing this fragment must implement it's callbacks.
        Activity activity = getActivity();
        if(!(activity instanceof OnSaveClicked)) {
            throw new ClassCastException(activity.getClass().getSimpleName()
                    + " must implement AddEditActivityFragment.OnSaveClicked interface");
        }
        mSaveListener = (OnSaveClicked) activity;
    }

    // @Override
    public void onViewCreated(@Nullable Bundle savedInstanceState) {
        // super.onViewCreated(savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach: starts");
        super.onDetach();
        mSaveListener = null;
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: starts");
        View view = inflater.inflate(R.layout.add_edit_trip_fragment, container, false);

        nameTextView = view.findViewById(R.id.name_column);
        destinationTextView = view.findViewById(R.id.destination_column);
        dateTextView = view.findViewById(R.id.date_column);
        riskAssessmentTextView = view.findViewById(R.id.risk_assessment_column);
        descriptionTextView = view.findViewById(R.id.description_column);
        durationTextView = view.findViewById(R.id.duration_column);
        aimTextView = view.findViewById(R.id.aim_column);
        statusTextView = view.findViewById(R.id.status_column);
        Button saveButton = view.findViewById(R.id.add_trip_button);

        Bundle arguments = getArguments();
        final Trip trip;
        if(arguments != null) {
            Log.d(TAG, "onCreateView: retrieving task details.");

            trip = (Trip) arguments.getSerializable(Trip.class.getSimpleName());
            if(trip != null) {
                Log.d(TAG, "onCreateView: Task details found, editing...");
                nameTextView.setText(trip.getName());
                destinationTextView.setText(trip.getDestination());
                dateTextView.setText(trip.getDate());
                riskAssessmentTextView.setText(trip.getRisk_assessment());
                descriptionTextView.setText(trip.getDescription());
                durationTextView.setText(trip.getDuration());
                aimTextView.setText(trip.getAim());
                statusTextView.setText(trip.getStatus());
                mMode = FragmentEditMode.EDIT;
            } else {
                // No task, so we must be adding a new task, and not editing an  existing one
                mMode = FragmentEditMode.ADD;
            }
        } else {
            trip = null;
            Log.d(TAG, "onCreateView: No arguments, adding new record");
            mMode = FragmentEditMode.ADD;
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update the database if at least one field has changed.
                // - There's no need to hit the database unless this has happened.
                int so;     // to save repeated conversions to int.
                if(mSortOrderTextView.length()>0) {
                    so = Integer.parseInt(mSortOrderTextView.getText().toString());
                } else {
                    so = 0;
                }

                ContentResolver contentResolver = getActivity().getContentResolver();
                ContentValues values = new ContentValues();

                switch (mMode) {
                    case EDIT:
                        /*
                        if(trip == null) {
                            // remove lint warnings, will never execute
                            break;
                        }
                        if(!nameTextView.getText().toString().equals(trip.getName())) {
                            values.put(TasksContract.Columns.TASKS_NAME, nameTextView.getText().toString());
                        }
                        if(!mDescriptionTextView.getText().toString().equals(trip.getDescription())) {
                            values.put(TasksContract.Columns.TASKS_DESCRIPTION, mDescriptionTextView.getText().toString());
                        }
                        if(so != task.getSortOrder()) {
                            values.put(TasksContract.Columns.TASKS_SORTORDER, so);
                        }
                        if(values.size() != 0) {
                            Log.d(TAG, "onClick: updating task");
                            contentResolver.update(TasksContract.buildTaskUri(trip.getId()), values, null, null);
                        }
                        */
                        break;
                    case ADD:
                        if(nameTextView.length()>0) {
                            MyDatabaseHelper database = new MyDatabaseHelper(getContext());
                            database.insertTrip(
                                    nameTextView.toString(), destinationTextView.toString(), dateTextView.toString(), riskAssessmentTextView.toString(),
                                    descriptionTextView.toString(), durationTextView.toString(), aimTextView.toString(), statusTextView.toString()
                            );
                        }
                        break;
                }
                Log.d(TAG, "onClick: Done editing");

                if(mSaveListener != null) {
                    mSaveListener.onSaveClicked();
                }
            }
        });
        Log.d(TAG, "onCreateView: Exiting...");

        return view;
    }
}
