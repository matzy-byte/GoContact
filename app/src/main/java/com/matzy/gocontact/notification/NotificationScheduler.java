package com.matzy.gocontact.notification;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.RequiresPermission;

import java.util.concurrent.TimeUnit;

public class NotificationScheduler {

    private static final int REQUEST_CODE = 123;

    @SuppressLint("ScheduleExactAlarm")
    @RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
    public static void scheduleRepeatingNotification(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, REQUEST_CODE, intent, PendingIntent.FLAG_IMMUTABLE);

        SharedPreferences prefs = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE);
        int intervalDays = prefs.getInt("interval", 15);

        long triggerAtMillis = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(intervalDays);

        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
        );
    }

    public static void cancelNotificationAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, REQUEST_CODE, intent, PendingIntent.FLAG_IMMUTABLE);

        alarmManager.cancel(pendingIntent);
    }
}
