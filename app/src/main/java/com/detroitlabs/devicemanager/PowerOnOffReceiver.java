package com.detroitlabs.devicemanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.detroitlabs.devicemanager.sync.sequences.PowerOffTaskSequence;
import com.detroitlabs.devicemanager.sync.sequences.PowerOnTaskSequence;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;


public class PowerOnOffReceiver extends BroadcastReceiver {
    private static final String TAG = PowerOnOffReceiver.class.getName();

    @Inject
    PowerOnTaskSequence powerOnTaskSequence;

    @Inject
    PowerOffTaskSequence powerOffTaskSequence;

    @Override
    public void onReceive(Context context, Intent intent) {
        DmApplication.getInjector().inject(this);
        Log.d(TAG, "Action: " + intent.getAction());
        String action = intent.getAction();
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            powerOn();
        } else if (Intent.ACTION_SHUTDOWN.equals(action)) {
            powerOff();
        }
    }

    private void powerOn() {
        powerOnTaskSequence.run().subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                Log.d(TAG, "Notification is generated");
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e(TAG, "error generating Notification", throwable);
            }
        });
    }

    private void powerOff() {
        powerOffTaskSequence.run().subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                Log.d(TAG, "battery information is updated to server");
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e(TAG, "error updating battery information", throwable);
            }
        });
    }
}
