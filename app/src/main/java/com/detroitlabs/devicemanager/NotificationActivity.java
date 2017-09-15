package com.detroitlabs.devicemanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.detroitlabs.devicemanager.notification.DmNotification;
import com.detroitlabs.devicemanager.sync.Result;
import com.detroitlabs.devicemanager.sync.sequences.OwningNotificationSequence;

import javax.inject.Inject;

import io.reactivex.functions.BiConsumer;

public class NotificationActivity extends AppCompatActivity {

    private static final String TAG = NotificationActivity.class.getName();
    @Inject
    DmNotification dmNotification;

    @Inject
    OwningNotificationSequence owningNotificationSequence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DmApplication.getInjector().inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dmNotification.dismissAll();
    }

    @Override
    protected void onPause() {
        super.onPause();
        owningNotificationSequence.run().subscribe(new BiConsumer<Result, Throwable>() {
            @Override
            public void accept(Result result, Throwable throwable) throws Exception {
                if (throwable != null) {
                    Log.d(TAG, "fail to show notification", throwable);
                } else if (!result.isSuccess()) {
                    Log.d(TAG, "fail to show notification", result.exception);
                }
            }
        });
    }
}
