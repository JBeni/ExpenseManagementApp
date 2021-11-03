package com.example.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {

    Context context;
    List<Contacts> contactsList;
    RecyclerView recyclerView;
    final View.OnClickListener onClickListener = new MyOnClickListener();

    public static class ContactsViewHolder extends RecyclerView.ViewHolder {
        TextView rowId;
        TextView rowName;
        TextView rowEmail;

        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);
            rowId = itemView.findViewById(R.id.item_id);
            rowName = itemView.findViewById(R.id.item_name);
            rowEmail = itemView.findViewById(R.id.item_email);
        }
    }

    public ContactsAdapter(Context context, List<Contacts> contactsList, RecyclerView recyclerView) {
        this.context = context;
        this.contactsList = contactsList;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ContactsAdapter.ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.single_item, parent, false);
        view.setOnClickListener(onClickListener);

        ContactsViewHolder contactsViewHolder = new ContactsViewHolder(view);
        return contactsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAdapter.ContactsViewHolder holder, int position) {
        Contacts contacts = contactsList.get(position);
        holder.rowId.setText("" + contacts.getId());
        holder.rowName.setText("" + contacts.getName());
        holder.rowEmail.setText("" + contacts.getEmail());
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }


    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int itemPosition = recyclerView.getChildLayoutPosition(view);
            String item = contactsList.get(itemPosition).getName();
            Toast.makeText(context, item, Toast.LENGTH_SHORT).show();
        }
    }
}
