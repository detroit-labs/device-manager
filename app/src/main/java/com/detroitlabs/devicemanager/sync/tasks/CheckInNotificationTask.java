package com.detroitlabs.devicemanager.sync.tasks;

import android.content.Context;
import android.support.annotation.RestrictTo;

import com.detroitlabs.devicemanager.di.qualifiers.ApplicationContext;
import com.detroitlabs.devicemanager.notification.CheckInNotification;
import com.detroitlabs.devicemanager.notification.DmNotification;
import com.detroitlabs.devicemanager.sync.Result;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;

public class CheckInNotificationTask extends AsyncTask<Result> {

    private final Context context;
    private final DmNotification dmNotification;
    private String checkOutBy;

    @Inject
    public CheckInNotificationTask(@ApplicationContext Context context,
                                   DmNotification dmNotification) {
        this.context = context;
        this.dmNotification = dmNotification;
    }

    @Override
    protected void task(SingleEmitter<Result> emitter) {
        dmNotification.show(context, new CheckInNotification(context, checkOutBy));
        emitter.onSuccess(Result.success());
    }

    @RestrictTo(RestrictTo.Scope.SUBCLASSES)
    @Override
    public Single<Result> run() {
        if (checkOutBy == null || checkOutBy.isEmpty()) {
            return Single.error(new IllegalStateException("Please use run(String checkOutBy) instead"));
        }
        return super.run();
    }

    public Single<Result> run(String checkOutBy) {
        this.checkOutBy = checkOutBy;
        return run();
    }
}
