package com.detroitlabs.devicemanager.notification;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.detroitlabs.devicemanager.R;
import com.detroitlabs.devicemanager.SyncActivity;
import com.detroitlabs.devicemanager.di.qualifiers.ApplicationContext;
import com.detroitlabs.devicemanager.sync.DeviceUpdateService;
import com.detroitlabs.devicemanager.sync.SelfDeviceUpdateService;
import com.detroitlabs.devicemanager.utils.DeviceUtil;

import java.util.Collections;
import java.util.List;

import static com.detroitlabs.devicemanager.constants.Constants.DEVICE_UPDATE_NOTIFICATION_ID;
import static com.detroitlabs.devicemanager.sync.SelfDeviceUpdateService.ACTION_CHECK_IN;


public class CheckInNotification implements NotificationItem {
    private final Context context;
    private final String checkOutBy;

    public CheckInNotification(@ApplicationContext Context context,
                               String checkOutBy) {
        this.context = context;
        this.checkOutBy = checkOutBy;
    }

    @Override
    public int id() {
        return DEVICE_UPDATE_NOTIFICATION_ID;
    }

    @Override
    public String title() {
        return DeviceUtil.getBrandAndModel() + " - " + checkOutBy;
    }

    @Override
    public String message() {
        return "Check in before shutting down and returning the phone";
    }

    @Override
    public int smallIcon() {
        return R.mipmap.ic_launcher;
    }

    @Override
    public List<NotificationCompat.Action> actions() {
        Intent deviceUpdateIntent = new Intent(context, SelfDeviceUpdateService.class);
        deviceUpdateIntent.setAction(ACTION_CHECK_IN);
        PendingIntent updatePendingIntent =
                PendingIntent.getService(
                        context,
                        0,
                        deviceUpdateIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action.Builder actionBuilder = new NotificationCompat.Action.Builder(R.drawable.ic_apple_grey600_24dp,
                "Check In", updatePendingIntent);
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
