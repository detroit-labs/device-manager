package com.detroitlabs.devicemanager.notification;


import android.app.PendingIntent;
import android.support.v4.app.NotificationCompat;

import java.util.List;

public interface NotificationItem {
    int id();

    String title();

    String message();

    int smallIcon();

    List<NotificationCompat.Action> actions();

    PendingIntent contentIntent();

    DmNotificationChannel channel();
}
