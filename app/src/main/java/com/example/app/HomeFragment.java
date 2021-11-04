package com.example.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.databinding.FragmentDashboardBinding;
import com.example.app.databinding.FragmentHomeBinding;
import com.example.app.ui.dashboard.DashboardViewModel;
import com.example.app.ui.home.HomeViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    DatabaseAdapter databaseAdapter;
    RecyclerView recyclerView;
    ContactsAdapter contactsAdapter;
    List<Contacts> contactsList = new ArrayList<>();

    private FragmentHomeBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        PreCreateDB.copyDB(getContext());
        databaseAdapter = new DatabaseAdapter(getContext());
        contactsList = databaseAdapter.getAllContacts();
        contactsAdapter = new ContactsAdapter(getContext(), contactsList, recyclerView);
        recyclerView.setAdapter(contactsAdapter);

        return root;
    }
}
