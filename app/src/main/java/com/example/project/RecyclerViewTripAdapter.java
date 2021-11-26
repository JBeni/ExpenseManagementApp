package com.example.project;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/*
    The links was used to guide me and to adjust to the needs using parts of code from

    https://stackoverflow.com/questions/26517855/using-the-recyclerview-with-a-database
    https://developer.android.com/guide/topics/ui/layout/recyclerview

    https://github.com/android/views-widgets-samples/tree/main/RecyclerView
    https://github.com/android/views-widgets-samples/tree/main/RecyclerViewAnimations
    https://github.com/android/views-widgets-samples/tree/main/RecyclerViewSimple
    https://www.dev2qa.com/android-cardview-with-image-and-text-example/
*/
public class RecyclerViewTripAdapter extends RecyclerView.Adapter<RecyclerViewTripAdapter.TripViewHolder> {

    private final Context context;
    private final Activity activity;
    private final List<Trip> trips;

    RecyclerViewTripAdapter(Activity activity, Context context, List<Trip> trips) {
        this.activity = activity;
        this.context = context;
        this.trips = trips;
    }

    // Source https://stackoverflow.com/questions/26517855/using-the-recyclerview-with-a-database
    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_trip_card_view_item, parent, false);
        return new TripViewHolder(view);
    }

    // https://www.dev2qa.com/android-cardview-with-image-and-text-example/
    @Override
    public void onBindViewHolder(@NonNull final TripViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.trip_name_txt.setText(trips.get(position).getName());

        holder.trip_main_grid.setOnClickListener(view -> {
            SharedTripViewModel sharedTripView = (SharedTripViewModel) context.getApplicationContext();
            sharedTripView.setSharedTripId(String.valueOf(trips.get(position).getId()));

            Intent intent = new Intent(context, MainTripExpensesActivity.class);
            intent.putExtra("trip_id", String.valueOf(trips.get(position).getId()));
            activity.startActivityForResult(intent, 1);
        });

        // https://stackoverflow.com/questions/34641240/toolbar-inside-cardview-to-create-a-popup-menu-overflow-icon
        holder.trip_menu_options.setOnClickListener(view -> showPopupMenu(holder.trip_menu_options, position));
    }

    // https://stackoverflow.com/questions/34641240/toolbar-inside-cardview-to-create-a-popup-menu-overflow-icon
    @SuppressLint("NonConstantResourceId")
    public void showPopupMenu(View view, int position) {
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        popup.getMenuInflater().inflate(R.menu.trip_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.view_trip_card_button:
                    Intent view_intent = new Intent(context, ViewTripActivity.class);
                    setTripExtraIntentData(view_intent, position);
                    activity.startActivityForResult(view_intent, 1);
                    return true;
                case R.id.edit_trip_card_button:
                    Intent edit_intent = new Intent(context, UpdateTripActivity.class);
                    setTripExtraIntentData(edit_intent, position);
                    activity.startActivityForResult(edit_intent, 1);
                    return true;
                case R.id.delete_trip_card_button:
                    confirmDeleteTripAndExpenses(String.valueOf(trips.get(position).getId()));
                    return true;
                default:
                    return false;
            }
        });
        popup.show();
    }

    private void setTripExtraIntentData(Intent intent, int position) {
        intent.putExtra("id", String.valueOf(trips.get(position).getId()));
        intent.putExtra("name", trips.get(position).getName());
        intent.putExtra("destination", trips.get(position).getDestination());
        intent.putExtra("date", trips.get(position).getDate());
        intent.putExtra("risk_assessment", trips.get(position).getRiskAssessment());
        intent.putExtra("description", trips.get(position).getDescription());
        intent.putExtra("duration", trips.get(position).getDuration());
        intent.putExtra("aim", trips.get(position).getAim());
        intent.putExtra("status", trips.get(position).getStatus());
    }

    private void confirmDeleteTripAndExpenses(String trip_id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete The Trip");
        builder.setMessage("Are you sure you want to delete the selected trip and the expenses related to it from the database?");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", (dialog, which) -> {
            SqliteDatabaseHandler databaseHandler = new SqliteDatabaseHandler(context);
            databaseHandler.deleteTripAndExpenses(trip_id);

            Intent intent = new Intent(context, MainTripActivity.class);
            activity.startActivityForResult(intent, 1);
        });
        builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public int getItemCount() { return getTripListSize(); }
    public int getTripListSize() {
        return trips.size();
    }

    static class TripViewHolder extends RecyclerView.ViewHolder {
        TextView trip_name_txt;
        CardView trip_main_grid;
        ImageButton trip_menu_options;

        TripViewHolder(@NonNull View itemView) {
            super(itemView);
            trip_name_txt = itemView.findViewById(R.id.trip_name_card_view);
            trip_main_grid = itemView.findViewById(R.id.main_content_trip_card_view);
            trip_menu_options = itemView.findViewById(R.id.trip_card_options);
        }
    }
}
