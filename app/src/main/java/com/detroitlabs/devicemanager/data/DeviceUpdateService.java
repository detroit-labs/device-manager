package com.detroitlabs.devicemanager.data;


import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

public class DeviceUpdateService extends IntentService {

    public static final String TAG = DeviceUpdateService.class.getSimpleName();

    public static final String ACTION_INSERT = TAG + ".INSERT";
    public static final String ACTION_BULK_INSERT = TAG + ".BULK_INSERT";
    private static final String EXTRA_VALUE = TAG + ".CONTENT_VALUES";

    public static void insertDevices(Context context, ContentValues contentValues) {
    }

    public DeviceUpdateService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
    }

}
