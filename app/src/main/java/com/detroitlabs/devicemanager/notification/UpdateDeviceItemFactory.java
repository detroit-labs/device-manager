package com.detroitlabs.devicemanager.notification;


import android.content.Context;

public class UpdateDeviceItemFactory implements NotificationItemFactory {

    public CheckOutNotification createCheckOutNotification(Context context) {
        return new CheckOutNotification(context);
    }
}
