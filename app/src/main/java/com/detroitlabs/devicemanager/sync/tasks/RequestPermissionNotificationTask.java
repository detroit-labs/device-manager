package com.detroitlabs.devicemanager.sync.tasks;


import android.content.Context;
import android.util.Log;

import com.detroitlabs.devicemanager.di.qualifiers.ApplicationContext;
import com.detroitlabs.devicemanager.notification.DmNotification;
import com.detroitlabs.devicemanager.notification.RequestPermissionNotification;
import com.detroitlabs.devicemanager.sync.Result;

import javax.inject.Inject;

import io.reactivex.SingleEmitter;

public class RequestPermissionNotificationTask extends AsyncTask<Result> {

    private static final String TAG = RequestPermissionNotificationTask.class.getName();
    private final Context context;
    private final DmNotification dmNotification;

    @Inject
    public RequestPermissionNotificationTask(@ApplicationContext Context context,
                                             DmNotification dmNotification) {
        this.context = context;
        this.dmNotification = dmNotification;
    }

    @Override
    protected void task(SingleEmitter<Result> emitter) {
        Log.d(TAG, "Start showing request permission notification");
        dmNotification.show(context, new RequestPermissionNotification(context));
        emitter.onSuccess(Result.success());
    }
}
