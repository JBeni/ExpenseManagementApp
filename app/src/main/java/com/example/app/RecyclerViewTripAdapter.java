package com.example.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * The link was used to guide me and to adjust to me needs using parts of code from
 * https://stackoverflow.com/questions/26517855/using-the-recyclerview-with-a-database
 **/
public class RecyclerViewTripAdapter extends RecyclerView.Adapter<RecyclerViewTripAdapter.TripViewHolder> {

    private Context context;
    private Activity activity;
    private List<Trip> trips;

    RecyclerViewTripAdapter(Activity activity, Context context, List<Trip> trips) {
        this.activity = activity;
        this.context = context;
        this.trips = trips;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_trip_card_view_item, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TripViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.trip_name_txt.setText(trips.get(position).getName().toString());

        holder.trip_main_grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedTripViewModel sharedTripView = (SharedTripViewModel) context.getApplicationContext();
                sharedTripView.setSharedTripId(String.valueOf(trips.get(position).getId()));

                Intent intent = new Intent(context, MainTripExpensesActivity.class);
                intent.putExtra("trip_id", String.valueOf(trips.get(position).getId()));
                activity.startActivityForResult(intent, 1);
            }
        });



/*
        holder.edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateTripActivity.class);
                intent.putExtra("id", String.valueOf(trips.get(position).getId()));

                activity.startActivityForResult(intent, 1);
            }
        });

        holder.delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateTripActivity.class);
                activity.startActivityForResult(intent, 1);

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Delete ?");
                builder.setMessage("Are you sure you want to delete ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //SqliteDatabaseHandler myDB = new SqliteDatabaseHandler(UpdateTripActivity.this);
                        //myDB.deleteOneRow(id);
                        //finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.create().show();
            }
        });
*/

        /**
         * https://stackoverflow.com/questions/34641240/toolbar-inside-cardview-to-create-a-popup-menu-overflow-icon
         */
        holder.trip_menu_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.trip_menu_options, position);
            }
        });
    }

    /**
     * https://stackoverflow.com/questions/34641240/toolbar-inside-cardview-to-create-a-popup-menu-overflow-icon
     */
    public void showPopupMenu(View view, int position) {
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        popup.getMenuInflater().inflate(R.menu.trip_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.view_trip_card_button:
                        Intent intent = new Intent(context, ViewTripActivity.class);

                        intent.putExtra("id", trips.get(position).getId());
                        intent.putExtra("name", trips.get(position).getName());
                        intent.putExtra("destination", trips.get(position).getDestination());
                        intent.putExtra("date", trips.get(position).getDate());
                        intent.putExtra("risk_assessment", trips.get(position).getRisk_assessment());
                        intent.putExtra("description", trips.get(position).getDescription());
                        intent.putExtra("duration", trips.get(position).getDuration());
                        intent.putExtra("aim", trips.get(position).getAim());
                        intent.putExtra("status", trips.get(position).getStatus());

                        activity.startActivityForResult(intent, 1);
                        return true;
                    case R.id.edit_trip_card_button:
                        Toast.makeText(view.getContext(), "Add to favourite", Toast.LENGTH_LONG).show();
                        return true;
                    case R.id.delete_trip_card_button:
                        //mDataSet.remove(position);
                        Toast.makeText(view.getContext(), "Done for now", Toast.LENGTH_LONG).show();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }

    @Override
    public int getItemCount() { return getTripListSize(); }
    public int getTripListSize() {
        return trips.size();
    }

    class TripViewHolder extends RecyclerView.ViewHolder {
        TextView trip_name_txt;
        CardView trip_main_grid;
        ImageButton trip_menu_options;

        TripViewHolder(@NonNull View itemView) {
            super(itemView);
            trip_name_txt = itemView.findViewById(R.id.trip_name_card_view);
            trip_main_grid = itemView.findViewById(R.id.main_content_trip_card_view);
            trip_menu_options = itemView.findViewById(R.id.trip_card_options);

            //Animate Recyclerview
            Animation translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            trip_main_grid.setAnimation(translate_anim);
        }
    }
}
