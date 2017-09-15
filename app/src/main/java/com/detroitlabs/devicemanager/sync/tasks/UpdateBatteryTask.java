package com.detroitlabs.devicemanager.sync.tasks;

import android.content.Context;
import android.util.Log;

import com.detroitlabs.devicemanager.data.DatabaseContract;
import com.detroitlabs.devicemanager.di.qualifiers.ApplicationContext;
import com.detroitlabs.devicemanager.specification.CanUpdateDevice;
import com.detroitlabs.devicemanager.sync.Result;
import com.detroitlabs.devicemanager.utils.DeviceUtil;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;

import io.reactivex.SingleEmitter;

import static com.detroitlabs.devicemanager.data.DatabaseContract.TABLE_DEVICES;


public class UpdateBatteryTask extends SyncDeviceTask {

    private static final String TAG = UpdateBatteryTask.class.getName();
    private final Context context;

    @Inject
    UpdateBatteryTask(@ApplicationContext Context context,
                      CanUpdateDevice canUpdateDevice) {
        super(canUpdateDevice);
        this.context = context;
    }

    @Override
    protected void task(SingleEmitter<Result> emitter) {
        double batteryPct = DeviceUtil.getBatteryLevel(context);
        Log.d(TAG, "Update battery percentage: " + batteryPct);
        FirebaseDatabase.getInstance().getReference()
                .child(TABLE_DEVICES)
                .child(DeviceUtil.getSerialNumber())
                .child(DatabaseContract.DeviceColumns.LAST_KNOWN_BATTERY)
                .setValue(batteryPct);
        emitter.onSuccess(Result.success());
    }
}
