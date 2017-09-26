package com.detroitlabs.devicemanager.sync.tasks;


import android.content.Context;
import android.util.Log;

import com.detroitlabs.devicemanager.db.Device;
import com.detroitlabs.devicemanager.di.qualifiers.ApplicationContext;
import com.detroitlabs.devicemanager.specification.CanUpdateDevice;
import com.detroitlabs.devicemanager.sync.Result;
import com.detroitlabs.devicemanager.utils.DeviceUtil;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;

import io.reactivex.SingleEmitter;

import static com.detroitlabs.devicemanager.data.DatabaseContract.DeviceColumns.BRAND_AND_MODEL;
import static com.detroitlabs.devicemanager.data.DatabaseContract.DeviceColumns.IS_SAMSUNG;
import static com.detroitlabs.devicemanager.data.DatabaseContract.DeviceColumns.PLATFORM;
import static com.detroitlabs.devicemanager.data.DatabaseContract.DeviceColumns.SCREEN_RESOLUTION;
import static com.detroitlabs.devicemanager.data.DatabaseContract.DeviceColumns.SCREEN_SIZE;
import static com.detroitlabs.devicemanager.data.DatabaseContract.DeviceColumns.VERSION;
import static com.detroitlabs.devicemanager.data.DatabaseContract.DeviceColumns.YEAR_CLASS;
import static com.detroitlabs.devicemanager.data.DatabaseContract.TABLE_DEVICES;

public class RegisterTask extends UpdateDeviceTask {

    private static final String TAG = RegisterTask.class.getName();
    private final Context context;

    @Inject
    public RegisterTask(@ApplicationContext Context context,
                        CanUpdateDevice canUpdateDevice) {
        super(canUpdateDevice);
        this.context = context;
    }

    @Override
    protected void task(final SingleEmitter<Result> emitter) {
        Log.d(TAG, "start register task");
        Device selfDevice = DeviceUtil.readThisDevice(context);
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference deviceRef = dbRef.child(TABLE_DEVICES).child(selfDevice.serialNumber);
        deviceRef.child(PLATFORM).setValue(selfDevice.platform);
        deviceRef.child(BRAND_AND_MODEL).setValue(selfDevice.brandAndModel);
        deviceRef.child(VERSION).setValue(selfDevice.version);
        deviceRef.child(SCREEN_SIZE).setValue(selfDevice.screenSize);
        deviceRef.child(SCREEN_RESOLUTION).setValue(selfDevice.screenResolution);
        deviceRef.child(YEAR_CLASS).setValue(selfDevice.yearClass);
        deviceRef.child(IS_SAMSUNG).setValue(selfDevice.isSamsung);
        emitter.onSuccess(Result.success());
    }
}
