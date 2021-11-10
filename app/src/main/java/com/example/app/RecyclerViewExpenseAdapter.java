package com.example.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
                Intent view_intent = new Intent(context, ViewExpenseActivity.class);
                setExpenseExtraIntentData(view_intent, position);
                activity.startActivityForResult(view_intent, 1);
            }
        });

        /**
         * https://stackoverflow.com/questions/34641240/toolbar-inside-cardview-to-create-a-popup-menu-overflow-icon
         */
        holder.expense_menu_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.expense_menu_options, position);
            }
        });
    }

    /**
     * https://stackoverflow.com/questions/34641240/toolbar-inside-cardview-to-create-a-popup-menu-overflow-icon
     */
    private void showPopupMenu(View view, int position) {
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        popup.getMenuInflater().inflate(R.menu.expense_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit_expense_card_button:
                        Intent edit_intent = new Intent(context, UpdateTripActivity.class);
                        setExpenseExtraIntentData(edit_intent, position);
                        activity.startActivityForResult(edit_intent, 1);
                        return true;
                    case R.id.delete_expense_card_button:
                        confirmDeleteExpense(String.valueOf(expenses.get(position).getId()));
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }

    private void confirmDeleteExpense(String expense_id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete The Trip");
        builder.setMessage("Are you sure you want to delete the selected trip and the expenses related to it from the database?");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SqliteDatabaseHandler databaseHandler = new SqliteDatabaseHandler(context);
                databaseHandler.deleteExpense(expense_id);

                Intent intent = new Intent(context, MainTripExpensesActivity.class);
                activity.startActivityForResult(intent, 1);
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

    private void setExpenseExtraIntentData(Intent intent, int position) {
        intent.putExtra("id", String.valueOf(expenses.get(position).getId()));
        intent.putExtra("type", expenses.get(position).getType());
        intent.putExtra("amount", expenses.get(position).getAmount());
        intent.putExtra("time", expenses.get(position).getTime());
        intent.putExtra("additional_comments", expenses.get(position).getAdditional_comments());
        intent.putExtra("trip_id", expenses.get(position).getTripId());
    }

    @Override
    public int getItemCount() { return getExpenseListSize(); }
    public int getExpenseListSize() {
        return expenses.size();
    }

    protected class ExpenseViewHolder extends RecyclerView.ViewHolder {
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
