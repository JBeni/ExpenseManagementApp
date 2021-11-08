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

public class RecyclerViewExpenseAdapter extends RecyclerView.Adapter<RecyclerViewExpenseAdapter.ExpenseViewHolder> {

    private Context context;
    private Activity activity;
    private List<Expense> expenses;

    RecyclerViewExpenseAdapter(Activity activity, Context context, List<Expense> expenses) {
        this.activity = activity;
        this.context = context;
        this.expenses = expenses;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_expense_card_view_item, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ExpenseViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.expense_type_txt.setText(expenses.get(position).getType().toString());

        holder.expense_main_grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewTripExpenseActivity.class);
                activity.startActivityForResult(intent, 1);
            }
        });

        /**
         * https://stackoverflow.com/questions/34641240/toolbar-inside-cardview-to-create-a-popup-menu-overflow-icon
         */
        holder.expense_menu_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.expense_menu_options);
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
    public int getItemCount() { return getExpenseListSize(); }
    public int getExpenseListSize() {
        return expenses.size();
    }

    class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView expense_type_txt;
        CardView expense_main_grid;
        ImageButton expense_menu_options;

        ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            expense_type_txt = itemView.findViewById(R.id.expense_type_card_view);
            expense_main_grid = itemView.findViewById(R.id.main_content_expense_card_view);
            expense_menu_options = itemView.findViewById(R.id.expense_card_options);

            //Animate Recyclerview
            Animation translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            expense_main_grid.setAnimation(translate_anim);
        }
    }
}
