package com.detroitlabs.devicemanager.notification;


import android.app.Notification;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

public class DefaultNotificationBuilder implements NotificationBuilder {
    @Override
    public Notification build(Context context, NotificationItem item) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, item.channel().channelId())
                        .setContentTitle(item.title())
                        .setContentText(item.message())
                        .setCategory(NotificationCompat.CATEGORY_STATUS)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setOngoing(true)
                        .setLocalOnly(true)
                        .setAutoCancel(false)
                        .setSmallIcon(item.smallIcon())
                        .setContentIntent(item.contentIntent());
        for (NotificationCompat.Action action : item.actions()) {
            builder.addAction(action);
        }
        return builder.build();
    }
}
