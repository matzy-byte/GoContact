package com.matzy.gocontact.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationManagerCompat;
import androidx.room.Room;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.matzy.gocontact.data.Contact;
import com.matzy.gocontact.data.ContactDAO;
import com.matzy.gocontact.data.ContactDatabase;
import com.matzy.gocontact.worker.NotificationWorker;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NotificationActionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {

            OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                    .setInitialDelay(1, TimeUnit.MINUTES)
                    .build();

            WorkManager.getInstance(context).enqueue(request);
            return;
        }

        if (intent != null && NotificationWorker.ACTION_NOTIFY.equals(intent.getAction())) {
            int contactId = intent.getIntExtra(NotificationWorker.EXTRA_CONTACT_ID, -1);
            if (contactId != -1) {
                ContactDatabase db = Room.databaseBuilder(context, ContactDatabase.class, "contact_table").build();
                ContactDAO dao = db.contactDAO();
                Executors.newSingleThreadExecutor().execute(() -> {
                    List<Contact> contacts = dao.getAllContacts();
                    Contact contact = contacts.get((int) (Math.random() * (contacts.size() - 1)));
                    if (contact != null && contact.id == contactId) {
                        dao.update(contact);
                    }

                    NotificationManagerCompat.from(context).cancel(contactId);

                    OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                            .setInitialDelay(1, TimeUnit.MINUTES)
                            .build();
                    WorkManager.getInstance(context).enqueue(request);
                });
            }
        }
    }
}
