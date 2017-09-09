package com.detroitlabs.devicemanager.notification;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;

import com.detroitlabs.devicemanager.R;
import com.detroitlabs.devicemanager.SyncActivity;
import com.detroitlabs.devicemanager.sync.SelfDeviceUpdateService;
import com.detroitlabs.devicemanager.utils.DeviceUtil;

import java.util.Collections;
import java.util.List;

import static com.detroitlabs.devicemanager.constants.Constants.DEVICE_UPDATE_NOTIFICATION_ID;
import static com.detroitlabs.devicemanager.constants.Constants.KEY_QUICK_CHECKOUT;
import static com.detroitlabs.devicemanager.sync.SelfDeviceUpdateService.ACTION_CHECK_OUT;

public class CheckOutNotification implements NotificationItem {

    private final Context context;

    public CheckOutNotification(Context context) {
        this.context = context;
    }

    @Override
    public int id() {
        return DEVICE_UPDATE_NOTIFICATION_ID;
    }

    @Override
    public String title() {
        return DeviceUtil.getBrandAndModel();
    }

    @Override
    public String message() {
        return "Check out to borrow this device";
    }

    @Override
    public int smallIcon() {
        return R.mipmap.ic_launcher;
    }

    @Override
    public List<NotificationCompat.Action> actions() {
        Intent deviceUpdateIntent = new Intent(context, SelfDeviceUpdateService.class);
        deviceUpdateIntent.setAction(ACTION_CHECK_OUT);
        PendingIntent updatePendingIntent =
                PendingIntent.getService(
                        context,
                        0,
                        deviceUpdateIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action.Builder actionBuilder = new NotificationCompat.Action.Builder(R.drawable.ic_apple_grey600_24dp,
                "Check Out", updatePendingIntent);
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_QUICK_CHECKOUT)
                .setLabel("Type your name")
                .build();
        actionBuilder.addRemoteInput(remoteInput);
        return Collections.singletonList(actionBuilder.build());
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
