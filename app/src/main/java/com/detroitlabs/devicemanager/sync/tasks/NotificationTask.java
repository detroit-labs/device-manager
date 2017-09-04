package com.detroitlabs.devicemanager.sync.tasks;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;

import com.detroitlabs.devicemanager.PagerActivity;
import com.detroitlabs.devicemanager.R;
import com.detroitlabs.devicemanager.sync.DeviceUpdateService;
import com.detroitlabs.devicemanager.utils.DeviceUtil;

import io.reactivex.SingleEmitter;

import static com.detroitlabs.devicemanager.constants.Constants.KEY_QUICK_CHECKOUT;
import static com.detroitlabs.devicemanager.constants.Constants.NOTIFICATION_CHANNEL_ID;
import static com.detroitlabs.devicemanager.constants.Constants.NOTIFICATION_ID;

abstract class NotificationTask extends AsyncTask<Boolean> {

    protected final Context context;

    public NotificationTask(Context context) {
        this.context = context;
    }

    @Override
    protected void task(SingleEmitter<Boolean> emitter) {
        String contentTitle = DeviceUtil.getBrandAndModel();
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                        .setContentTitle(contentTitle)
                        .setContentText(getContentText())
                        .setCategory(NotificationCompat.CATEGORY_STATUS)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setOngoing(true)
                        .setLocalOnly(true)
                        .setSmallIcon(R.mipmap.ic_launcher);


        Intent deviceUpdateIntent = new Intent(context, DeviceUpdateService.class);
        deviceUpdateIntent.setAction(getDeviceUpdateIntentAction());
        PendingIntent updatePendingIntent =
                PendingIntent.getService(
                        context,
                        0,
                        deviceUpdateIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_QUICK_CHECKOUT)
                .setLabel("Type your name")
                .build();
        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(R.drawable.ic_apple_grey600_24dp, getActionTitle(), updatePendingIntent)
                        .addRemoteInput(remoteInput)
                        .build();
        builder.addAction(action);

        Intent resultIntent = new Intent(context, PagerActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
        emitter.onSuccess(true);
    }

    protected abstract CharSequence getActionTitle();

    protected abstract String getDeviceUpdateIntentAction();

    protected abstract CharSequence getContentText();
}
