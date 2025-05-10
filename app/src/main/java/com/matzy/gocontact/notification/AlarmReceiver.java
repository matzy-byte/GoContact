package com.matzy.gocontact.notification;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.RequiresPermission;

public class AlarmReceiver extends BroadcastReceiver {
    @SuppressLint("ScheduleExactAlarm")
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationUtils.sendNotification(context);
        NotificationScheduler.scheduleRepeatingNotification(context);
    }
}
