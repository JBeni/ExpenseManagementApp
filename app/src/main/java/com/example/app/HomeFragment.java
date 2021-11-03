package com.example.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    DatabaseAdapter databaseAdapter;
    RecyclerView recyclerView;
    ContactsAdapter contactsAdapter;
    List<Contacts> contactsList = new ArrayList<>();

    public HomeFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        PreCreateDB.copyDB(getContext());
        databaseAdapter = new DatabaseAdapter(getContext());
        contactsList = databaseAdapter.getAllContacts();
        contactsAdapter = new ContactsAdapter(getContext(), contactsList, recyclerView);
        recyclerView.setAdapter(contactsAdapter);

        return view;
    }
}
