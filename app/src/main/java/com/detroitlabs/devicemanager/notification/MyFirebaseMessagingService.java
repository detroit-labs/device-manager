package com.detroitlabs.devicemanager.notification;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "DATA: " + remoteMessage.getData());
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Notification body: " + remoteMessage.getNotification().getBody());
        }
    }
}
