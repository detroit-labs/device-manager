package com.detroitlabs.devicemanager.sync.tasks;

import android.content.Context;

import com.detroitlabs.devicemanager.di.qualifiers.ApplicationContext;
import com.detroitlabs.devicemanager.notification.DmNotification;
import com.detroitlabs.devicemanager.notification.LogInNotification;
import com.detroitlabs.devicemanager.sync.Result;

import javax.inject.Inject;

import io.reactivex.SingleEmitter;


public class LogInNotificationTask extends AsyncTask<Result> {

    private final Context context;
    private final DmNotification dmNotification;

    @Inject
    LogInNotificationTask(@ApplicationContext Context context,
                          DmNotification dmNotification) {

        this.context = context;
        this.dmNotification = dmNotification;
    }

    @Override
    protected void task(SingleEmitter<Result> emitter) {
        dmNotification.show(context, new LogInNotification(context));
        emitter.onSuccess(Result.success());
    }
}
