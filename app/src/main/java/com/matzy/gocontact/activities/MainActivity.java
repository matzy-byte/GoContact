package com.matzy.gocontact.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.matzy.gocontact.R;
import com.matzy.gocontact.data.Contact;
import com.matzy.gocontact.notification.NotificationScheduler;
import com.matzy.gocontact.viewmodel.ContactAdapter;
import com.matzy.gocontact.viewmodel.ContactViewModel;

public class MainActivity extends AppCompatActivity implements ContactFormDialogFragment.ContactFormListener, ContactDialogFragment.ContactListener, ContactDeleteDialogFragment.ContactListener {
    private ContactViewModel contactViewModel;
    private ContactAdapter adapter;

    @SuppressLint("ScheduleExactAlarm")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        if (intent.getAction() != null && !intent.getAction().equals(Intent.ACTION_MAIN)) {
            finish();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 100);
            }
        }

        setContentView(R.layout.main_activity);

        RecyclerView recyclerView = findViewById(R.id.recycler_contacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ContactAdapter();
        recyclerView.setAdapter(adapter);

        contactViewModel = new ViewModelProvider(this).get(ContactViewModel.class);
        contactViewModel.getAllContacts().observe(this, contacts -> {
            adapter.setContacts(contacts);
            ContactAdapter.OnItemClickListener a = new ContactAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Contact contact) {
                    openContactDialog(contact);
                }

                @Override
                public void onDeleteItemClick(Contact contact) {
                    openContactDeleteDialog(contact);
                }
            };
            adapter.setOnItemClickListener(a);
        });

        FloatingActionButton fab = findViewById(R.id.fab_add_contact);
        fab.setOnClickListener(v -> openContactFormDialog());

        ImageButton btn_pref = findViewById(R.id.btn_preferences);
        btn_pref.setOnClickListener(v -> openPreferencesDialog());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            if (!alarmManager.canScheduleExactAlarms()) {
                intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(intent);
            }
        }

        SharedPreferences prefs = getSharedPreferences("app_settings", Context.MODE_PRIVATE);
        if (!prefs.contains("notify")) {
            prefs.edit().putBoolean("notify", false).apply();
            prefs.edit().putString("time", "00:00").apply();
            prefs.edit().putInt("interval", 7).apply();
            prefs.edit().putString("theme", "Light").apply();
            prefs.edit().putString("algo", "Priority").apply();
            openPreferencesDialog();
        }

        TextView notificationInfo = findViewById(R.id.txt_notification_status);
        Button notificationStatus = findViewById(R.id.btn_toggle_notifications);

        if (prefs.getBoolean("notify", true)) {
            notificationInfo.setText(String.format("%s: %s", getString(R.string.notifications), getString(R.string.on)));
            notificationStatus.setText(R.string.deactivate);
        } else {
            notificationInfo.setText(String.format("%s: %s", getString(R.string.notifications), getString(R.string.off)));
            notificationStatus.setText(R.string.activate);
        }

        notificationStatus.setOnClickListener(v -> {
            if (prefs.getBoolean("notify", true)) {
                NotificationScheduler.cancelNotificationAlarm(this);
                prefs.edit().putBoolean("notify", false).apply();
                notificationInfo.setText(String.format("%s: %s", getString(R.string.notifications), getString(R.string.off)));
                notificationStatus.setText(R.string.activate);
            } else {
                NotificationScheduler.scheduleRepeatingNotification(this);
                prefs.edit().putBoolean("notify", true).apply();
                notificationInfo.setText(String.format("%s: %s", getString(R.string.notifications), getString(R.string.on)));
                notificationStatus.setText(R.string.deactivate);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, R.string.required_info, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onSaveClicked(Contact contact) {
        contactViewModel.upsert(contact);
    }

    @Override
    public void onEditClicked(Contact contact) {
        openContactFormDialog(contact);
    }

    @Override
    public void onRemoveClicked(Contact contact) {
        contactViewModel.delete(contact);
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

    private void openContactDeleteDialog(Contact contact) {
        ContactDeleteDialogFragment dialog = ContactDeleteDialogFragment.newInstance(contact);
        dialog.show(getSupportFragmentManager(), "ContactDeleteDialogFragment");
    }

    private void openPreferencesDialog() {
        PreferencesDialogFragment dialog = new PreferencesDialogFragment();
        dialog.show(getSupportFragmentManager(), "PreferencesDialogFragment");
    }
}
