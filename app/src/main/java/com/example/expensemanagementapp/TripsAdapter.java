package com.example.expensemanagementapp;

import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.TripsViewHolder> {

    private Cursor cursor;
    private OnTaskClickListener listener;

    interface OnTaskClickListener {
        void onEditClick(Trip task);
        void onDeleteClick(Trip task);
    }

    public TripsAdapter(Cursor cursor, OnTaskClickListener listener) {
        this.cursor = cursor;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TripsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home, parent, false);
        return new TripsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripsViewHolder holder, int position) {
        if((cursor == null) || (cursor.getCount() == 0)) {
            holder.name.setText(R.string.instructions_heading);
            holder.description.setText(R.string.instructions);
            holder.editButton.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);
        } else {
            if(!cursor.moveToPosition(position)) {
                throw new IllegalStateException("Couldn't move cursor to position " + position);
            }

            final Trip trip = new Trip(mCursor.getLong(mCursor.getColumnIndex(TasksContract.Columns._ID)),
                    mCursor.getString(mCursor.getColumnIndex(TasksContract.Columns.TASKS_NAME)),
                    mCursor.getString(mCursor.getColumnIndex(TasksContract.Columns.TASKS_DESCRIPTION)),
                    mCursor.getInt(mCursor.getColumnIndex(TasksContract.Columns.TASKS_SORTORDER)));

            holder.name.setText(task.getName());
            holder.description.setText(task.getDescription());
            holder.editButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setVisibility(View.VISIBLE);

            View.OnClickListener buttonListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Log.d(TAG, "onClick: starts");
                    switch(view.getId()) {
                        case R.id.tli_edit:
                            if(listener != null) {
                                listener.onEditClick(trip);
                            }
                            break;
                        case R.id.tli_delete:
                            if(listener != null) {
                                listener.onDeleteClick(trip);
                            }
                            break;
                        default:
                            break;
                    }
                }
            };

            holder.editButton.setOnClickListener(buttonListener);
            holder.deleteButton.setOnClickListener(buttonListener);
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class TripsViewHolder extends RecyclerView.ViewHolder {

        TripsViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
