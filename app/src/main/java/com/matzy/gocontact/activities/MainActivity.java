package com.matzy.gocontact.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.matzy.gocontact.R;
import com.matzy.gocontact.data.Contact;
import com.matzy.gocontact.viewmodel.ContactAdapter;
import com.matzy.gocontact.viewmodel.ContactViewModel;
import com.matzy.gocontact.worker.NotificationWorker;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements ContactFormDialogFragment.ContactFormListener, ContactDialogFragment.ContactListener {
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
            adapter.setOnItemClickListener(this::openContactDialog);
        });

        FloatingActionButton fab = findViewById(R.id.fab_add_contact);
        fab.setOnClickListener(v -> openContactFormDialog());

        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                .setInitialDelay(5, TimeUnit.SECONDS) // For testing, change to 24 hours in prod
                .build();
        WorkManager.getInstance(this).enqueue(request);
    }

    @Override
    public void onSaveClicked(Contact contact) {
        contactViewModel.upsert(contact);
    }

    @Override
    public void onEditClicked(Contact contact) {
        openContactFormDialog(contact);
    }

    private void openContactFormDialog(Contact contact) {
        ContactFormDialogFragment dialog = ContactFormDialogFragment.newInstance(contact);
        dialog.show(getSupportFragmentManager(), "ContactFormDialogFragment");
    }

    private void openContactFormDialog() {
        ContactFormDialogFragment dialog = ContactFormDialogFragment.newInstance();
        dialog.show(getSupportFragmentManager(), "ContactFormDialogFragment");
    }

    private void openContactDialog(Contact contact) {
        ContactDialogFragment dialog = ContactDialogFragment.newInstance(contact);
        dialog.show(getSupportFragmentManager(), "ContactDialogFragment");
    }
}
