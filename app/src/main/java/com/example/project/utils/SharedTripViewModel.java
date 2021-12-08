package com.example.project.utils;

import android.app.Application;

public class SharedTripViewModel extends Application {

    private String shared_trip_id;

    public String getSharedTripId() {
        return shared_trip_id;
    }

    public void setSharedTripId(String new_trip_id) {
        this.shared_trip_id = new_trip_id;
    }
}
