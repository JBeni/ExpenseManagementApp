package com.example.project.recycler;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project.R;
import com.example.project.models.Trip;
import java.util.List;

public class RecyclerViewSearchTripAdapter extends RecyclerView.Adapter<RecyclerViewSearchTripAdapter.SearchTripViewHolder> {

    private final Context context;
    private final Activity activity;
    private final List<Trip> trips;

    public RecyclerViewSearchTripAdapter(Activity activity, Context context, List<Trip> trips) {
        this.activity = activity;
        this.context = context;
        this.trips = trips;
    }

    @NonNull
    @Override
    public SearchTripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_search_trip_item, parent, false);
        return new SearchTripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchTripViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.search_trip_name_txt.setText(trips.get(position).getName());

        holder.search_trip_main_grid.setOnClickListener(view -> viewSearchTripDialog(trips.get(position)));
    }

    private void viewSearchTripDialog(Trip trip) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Selected Trip");
        builder.setCancelable(false);

        String message = "Name: " + trip.getName() + "\n" +
                "Destination: " + trip.getDestination() + "\n" +
                "Date: " + trip.getDate() + "\n" +
                "Risk Assessment: " + trip.getRiskAssessment() + "\n" +
                "Description: " + trip.getDescription() + "\n" +
                "Duration: " + trip.getDuration() + "\n" +
                "Aim: " + trip.getAim() + "\n" +
                "Status: " + trip.getStatus() + "\n";
        builder.setMessage(message);
        builder.setNegativeButton("Close", (dialog, which) -> dialog.cancel());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public int getItemCount() { return getTripListSize(); }
    public int getTripListSize() {
        return trips.size();
    }

    static class SearchTripViewHolder extends RecyclerView.ViewHolder {
        TextView search_trip_name_txt;
        CardView search_trip_main_grid;

        SearchTripViewHolder(@NonNull View itemCardView) {
            super(itemCardView);
            search_trip_name_txt = itemCardView.findViewById(R.id.search_trip_item);
            search_trip_main_grid = itemCardView.findViewById(R.id.main_content_search_item);
        }
    }
}
