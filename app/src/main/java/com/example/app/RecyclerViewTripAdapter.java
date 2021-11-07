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
 *
 * Code taken https://github.com/stevdza-san/SQLite_Android-Complete_Tutorial
 *
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
        String beniamin = trips.get(position).getName();
        holder.trip_name_txt.setText(trips.get(position).getName().toString());

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewActivity.class);
                activity.startActivityForResult(intent, 1);
            }
        });

/*
        holder.edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id", String.valueOf(trips.get(position).getId()));

                activity.startActivityForResult(intent, 1);
            }
        });

        holder.delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateActivity.class);
                activity.startActivityForResult(intent, 1);

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Delete ?");
                builder.setMessage("Are you sure you want to delete ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //SqliteDatabaseHandler myDB = new SqliteDatabaseHandler(UpdateActivity.this);
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
                showPopupMenu(holder.trip_menu_options);
            }
        });
    }

    /**
     * https://stackoverflow.com/questions/34641240/toolbar-inside-cardview-to-create-a-popup-menu-overflow-icon
     */
    public void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        popup.getMenuInflater().inflate(R.menu.trip_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
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
        CardView mainLayout;
        ImageButton trip_menu_options;

        TripViewHolder(@NonNull View itemView) {
            super(itemView);
            trip_name_txt = itemView.findViewById(R.id.trip_name_card_view);
            mainLayout = itemView.findViewById(R.id.main_content_cardView);
            trip_menu_options = itemView.findViewById(R.id.trip_card_options);

            //Animate Recyclerview
            Animation translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            mainLayout.setAnimation(translate_anim);
        }
    }
}
