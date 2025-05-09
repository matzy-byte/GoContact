package com.matzy.gocontact.worker;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.room.Room;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.matzy.gocontact.R;
import com.matzy.gocontact.data.Contact;
import com.matzy.gocontact.data.ContactDAO;
import com.matzy.gocontact.data.ContactDatabase;
import com.matzy.gocontact.receiver.NotificationActionReceiver;

import java.util.List;

public class NotificationWorker extends Worker {
    public static final String CHANNEL_ID = "notify_channel";
    public static final String ACTION_NOTIFY = "com.example.ACTION_NOTIFY";
    public static final String EXTRA_CONTACT_ID = "contact_id";

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    @NonNull
    @Override
    public Result doWork() {
        ContactDatabase db = Room.databaseBuilder(getApplicationContext(), ContactDatabase.class, "contact_table").build();
        ContactDAO dao = db.contactDAO();
        List<Contact> contacts = dao.getAllContacts();
        Contact contact = contacts.get((int) (Math.random() * (contacts.size() - 1)));

        createNotificationChannel();

        Intent intent = new Intent(getApplicationContext(), NotificationActionReceiver.class);
        intent.setAction(ACTION_NOTIFY);
        intent.putExtra(EXTRA_CONTACT_ID, contact.id);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(),
                contact.id, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Contact Reminder")
                .setContentText("Reach out to " + contact.firstName)
                .addAction(R.drawable.ic_launcher_foreground, "Mark Done", pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
        manager.notify(contact.id, builder.build());

        return Result.success();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getApplicationContext().getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
}
