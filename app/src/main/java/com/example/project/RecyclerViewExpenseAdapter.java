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
public class RecyclerViewExpenseAdapter extends RecyclerView.Adapter<RecyclerViewExpenseAdapter.ExpenseViewHolder> {

    private final Context context;
    private final Activity activity;
    private final List<Expense> expenses;

    RecyclerViewExpenseAdapter(Activity activity, Context context, List<Expense> expenses) {
        this.activity = activity;
        this.context = context;
        this.expenses = expenses;
    }

    // Source https://stackoverflow.com/questions/26517855/using-the-recyclerview-with-a-database
    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_expense_card_view_item, parent, false);
        return new ExpenseViewHolder(view);
    }

    // https://www.dev2qa.com/android-cardview-with-image-and-text-example/
    @Override
    public void onBindViewHolder(@NonNull final ExpenseViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.expense_type_txt.setText(expenses.get(position).getType());

        holder.expense_main_grid.setOnClickListener(view -> {
            Intent view_intent = new Intent(context, ViewExpenseActivity.class);
            setExpenseExtraIntentData(view_intent, position);
            activity.startActivityForResult(view_intent, 1);
        });

        // https://stackoverflow.com/questions/34641240/toolbar-inside-cardview-to-create-a-popup-menu-overflow-icon
        holder.expense_menu_options.setOnClickListener(view -> showPopupMenu(holder.expense_menu_options, position));
    }

    // https://stackoverflow.com/questions/34641240/toolbar-inside-cardview-to-create-a-popup-menu-overflow-icon
    @SuppressLint("NonConstantResourceId")
    private void showPopupMenu(View view, int position) {
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        popup.getMenuInflater().inflate(R.menu.expense_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.edit_expense_card_button:
                    Intent edit_intent = new Intent(context, UpdateExpenseActivity.class);
                    setExpenseExtraIntentData(edit_intent, position);
                    activity.startActivityForResult(edit_intent, 1);
                    return true;
                case R.id.delete_expense_card_button:
                    confirmDeleteExpense(String.valueOf(expenses.get(position).getId()));
                    return true;
                default:
                    return false;
            }
        });
        popup.show();
    }

    private void confirmDeleteExpense(String expense_id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete The Expense");
        builder.setMessage("Are you sure you want to delete the selected expense from the database?");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", (dialog, which) -> {
            SqliteDatabaseHandler databaseHandler = new SqliteDatabaseHandler(context);
            databaseHandler.deleteExpense(expense_id);

            Intent intent = new Intent(context, MainTripExpensesActivity.class);
            activity.startActivityForResult(intent, 1);
        });
        builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void setExpenseExtraIntentData(Intent intent, int position) {
        intent.putExtra("id", String.valueOf(expenses.get(position).getId()));
        intent.putExtra("type", expenses.get(position).getType());
        intent.putExtra("amount", expenses.get(position).getAmount());
        intent.putExtra("time", expenses.get(position).getTime());
        intent.putExtra("additional_comments", expenses.get(position).getAdditionalComments());
        intent.putExtra("trip_id", expenses.get(position).getTripId());
    }

    @Override
    public int getItemCount() { return getExpenseListSize(); }
    public int getExpenseListSize() {
        return expenses.size();
    }

    static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView expense_type_txt;
        CardView expense_main_grid;
        ImageButton expense_menu_options;

        ExpenseViewHolder(@NonNull View itemCardView) {
            super(itemCardView);
            expense_type_txt = itemCardView.findViewById(R.id.expense_type_card_view);
            expense_main_grid = iteitemCardViewmView.findViewById(R.id.main_content_expense_card_view);
            expense_menu_options = itemCardView.findViewById(R.id.expense_card_options);
        }
    }
}
