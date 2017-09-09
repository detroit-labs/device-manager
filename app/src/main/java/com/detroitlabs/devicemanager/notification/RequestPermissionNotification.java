package com.detroitlabs.devicemanager.notification;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.detroitlabs.devicemanager.R;
import com.detroitlabs.devicemanager.SyncActivity;
import com.detroitlabs.devicemanager.di.qualifiers.ApplicationContext;

import java.util.Collections;
import java.util.List;

import static com.detroitlabs.devicemanager.constants.Constants.DEVICE_UPDATE_NOTIFICATION_ID;

public class RequestPermissionNotification implements NotificationItem {

    private final Context context;

    public RequestPermissionNotification(@ApplicationContext Context context) {
        this.context = context;
    }

    @Override
    public int id() {
        return DEVICE_UPDATE_NOTIFICATION_ID;
    }

    @Override
    public String title() {
        return "Permission requested is pending";
    }

    @Override
    public String message() {
        return "Click to open the app";
    }

    @Override
    public int smallIcon() {
        return R.mipmap.ic_launcher;
    }

    @Override
    public List<NotificationCompat.Action> actions() {
        return Collections.emptyList();
    }

    @Override
    public PendingIntent contentIntent() {
        Intent resultIntent = new Intent(context, SyncActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public DmNotificationChannel channel() {
        return new DmNotificationChannel.UpdateDevice();
    }
}
