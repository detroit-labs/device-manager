package com.detroitlabs.devicemanager.notification;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.detroitlabs.devicemanager.data.DeviceUpdateService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.detroitlabs.devicemanager.constants.Constants.BROADCAST_ACTION_REQUEST_RECEIVED;
import static com.detroitlabs.devicemanager.constants.Constants.BROADCAST_EXTRA_REQUESTED_BY;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() != null) {
            String requestedBy = remoteMessage.getNotification().getBody();
            Log.d(TAG, "Receive request from: " + requestedBy);
            DeviceUpdateService.receiveRequest(getApplicationContext(), requestedBy);
            Intent intent = new Intent(BROADCAST_ACTION_REQUEST_RECEIVED);
            intent.putExtra(BROADCAST_EXTRA_REQUESTED_BY, requestedBy);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        }
    }
}
