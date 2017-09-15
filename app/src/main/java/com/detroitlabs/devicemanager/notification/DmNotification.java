package com.detroitlabs.devicemanager.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * After the device is booted, the notification will show up with
 * the current status (check out / check in).
 * If app is not in background:
 * 1. click the check out button and type name will check out this device
 * and notification will be updated with the name and a check in button
 * 2. click the check in button will check this device back in
 * and the notification will be updated with the checkout button and message
 * 3. click the content area will launch the app
 * If app is in foreground:(no notification should be shown)
 */
@Singleton
public class DmNotification {

    private final NotificationManager notificationManager;
    private final NotificationBuilder notificationBuilder;

    @Inject
    public DmNotification(NotificationManager notificationManager,
                          NotificationBuilder notificationBuilder) {

        this.notificationManager = notificationManager;
        this.notificationBuilder = notificationBuilder;
    }

    public void show(Context context, NotificationItem item) {
        Notification notification = notificationBuilder.build(context, item);
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
            createChannel(item);
        }
        notificationManager.notify(item.id(), notification);
    }

    public void dismissAll() {
        notificationManager.cancelAll();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannel(NotificationItem item) {
        NotificationChannel channel =
                new NotificationChannel(item.channel().channelId(),
                        item.channel().channelName(),
                        NotificationManagerCompat.IMPORTANCE_MAX);
        channel.setShowBadge(false);
        channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        channel.enableLights(false);
        channel.enableVibration(false);
        notificationManager.createNotificationChannel(channel);
    }
}
