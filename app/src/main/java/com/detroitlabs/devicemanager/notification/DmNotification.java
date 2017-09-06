package com.detroitlabs.devicemanager.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * After the device is booted, the notification will show up with
 * the current status (check out / check in).
 * If app is not in foreground:
 * 1. click the check out button and type name will check out this device
 * and notification will be updated with the name and a check in button
 * 2. click the check in button will check this device back in
 * and the notification will be updated with the checkout button and message
 * 3. click the content area will launch the app
 * If app is in foreground:
 * 1. same as when app is not in foreground
 * 2. same as when app is not in foreground
 * 3. click the content area will do nothing?
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
        notificationManager.notify(item.id(), notification);
    }
}
