package com.matzy.gocontact.notification;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresPermission;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.room.Room;

import com.matzy.gocontact.R;
import com.matzy.gocontact.data.Contact;
import com.matzy.gocontact.data.ContactDAO;
import com.matzy.gocontact.data.ContactDatabase;
import com.matzy.gocontact.data.ContactSelectionAlgorithm;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

public class NotificationUtils {
    private static final String CHANNEL_ID = "notify_channel";

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    public static void sendNotification(Context context) {
        Executors.newSingleThreadExecutor().execute(() -> {
            ContactDatabase db = Room.databaseBuilder(context, ContactDatabase.class, "contact_table").build();
            ContactDAO dao = db.contactDAO();
            List<Contact> contacts = dao.getAllContacts();
            Contact contact = ContactSelectionAlgorithm.selectContact(contacts);

            createNotificationChannel(context);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("Reminder")
                    .setContentText("Reach out to " + contact.firstName)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

            NotificationManagerCompat.from(context).notify(contact.id, builder.build());

            contact.latestInteraction = new Date();
            dao.update(contact);
        });
    }

    private static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Reminder Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
}

