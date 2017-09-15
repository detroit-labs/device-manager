package com.detroitlabs.devicemanager.sync;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.RemoteInput;
import android.util.Log;

import com.detroitlabs.devicemanager.DmApplication;
import com.detroitlabs.devicemanager.sync.tasks.CheckInNotificationTask;
import com.detroitlabs.devicemanager.sync.tasks.CheckOutNotificationTask;
import com.detroitlabs.devicemanager.sync.tasks.DeviceCheckInTask;
import com.detroitlabs.devicemanager.sync.tasks.DeviceCheckOutTask;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

import static com.detroitlabs.devicemanager.constants.Constants.KEY_QUICK_CHECKOUT;

public class SelfDeviceUpdateService extends Service {

    private static final String TAG = SelfDeviceUpdateService.class.getSimpleName();
    private static final String ACTION = TAG + ".ACTION";
    public static final String ACTION_CHECK_IN = ACTION + ".CHECK_IN";
    public static final String ACTION_CHECK_OUT = ACTION + ".CHECK_OUT";

    @Inject
    DeviceCheckOutTask checkOutTask;

    @Inject
    DeviceCheckInTask deviceCheckInTask;

    @Inject
    CheckOutNotificationTask checkOutNotificationTask;

    @Inject
    CheckInNotificationTask checkInNotificationTask;

    @Override
    public void onCreate() {
        super.onCreate();
        DmApplication.getInjector().inject(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (ACTION_CHECK_OUT.equals(action)) {
            performCheckOut(intent);
        } else if (ACTION_CHECK_IN.equals(action)) {
            performCheckIn();
        }
        return START_NOT_STICKY;
    }

    private void performCheckOut(Intent intent) {
        Log.d(TAG, "perform check out");
        final CharSequence name = getName(intent);
        checkOutTask.run(name.toString()).subscribe(new Consumer<Result>() {
            @Override
            public void accept(Result result) throws Exception {
                if (result.isSuccess()) {
                    Log.d(TAG, "check out successful, update notification");
                    checkInNotificationTask.run(name.toString()).subscribe();
                } else {
                    Log.d(TAG, "check out fail", result.exception);
                    checkOutNotificationTask.run().subscribe();
                }
            }
        });
    }

    private void performCheckIn() {
        Log.d(TAG, "perform check in");
        deviceCheckInTask.run().subscribe(new Consumer<Result>() {
            @Override
            public void accept(Result result) throws Exception {
                Log.d(TAG, "check in result: " + result.isSuccess());
                checkOutNotificationTask.run().subscribe();
            }
        });
    }

    private CharSequence getName(Intent intent) {
        Bundle resultsFromIntent = RemoteInput.getResultsFromIntent(intent);
        if (resultsFromIntent != null) {
            return resultsFromIntent.getCharSequence(KEY_QUICK_CHECKOUT);
        }
        return "";
    }
}
