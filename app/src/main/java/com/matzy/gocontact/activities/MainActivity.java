package com.matzy.gocontact.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.matzy.gocontact.R;
import com.matzy.gocontact.data.Contact;
import com.matzy.gocontact.viewmodel.ContactAdapter;
import com.matzy.gocontact.viewmodel.ContactViewModel;

public class MainActivity extends AppCompatActivity {
    private ContactViewModel contactViewModel;
    private ContactAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        RecyclerView recyclerView = findViewById(R.id.recycler_contacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new ContactAdapter();
        recyclerView.setAdapter(adapter);

        contactViewModel = new ViewModelProvider(this).get(ContactViewModel.class);
        contactViewModel.getAllContacts().observe(this, contacts -> {
            adapter.setContacts(contacts);
        });

        // Testing
        contactViewModel.insert(new Contact("john", "doe", 5, "10.10.2022"));
    }
}
