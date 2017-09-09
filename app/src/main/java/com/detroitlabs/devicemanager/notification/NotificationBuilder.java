package com.detroitlabs.devicemanager.notification;


import android.app.Notification;
import android.content.Context;

public interface NotificationBuilder {
    Notification build(Context context, NotificationItem item);
}
