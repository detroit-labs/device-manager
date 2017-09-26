package com.detroitlabs.devicemanager.sync.tasks;


import com.detroitlabs.devicemanager.data.DatabaseContract;
import com.detroitlabs.devicemanager.specification.CanUpdateDevice;
import com.detroitlabs.devicemanager.sync.Result;
import com.detroitlabs.devicemanager.utils.DatetimeUtil;
import com.detroitlabs.devicemanager.utils.DeviceUtil;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;

import static com.detroitlabs.devicemanager.data.DatabaseContract.TABLE_DEVICES;

public class DeviceCheckOutTask extends UpdateDeviceTask {
    private String checkOutBy;

    @Inject
    public DeviceCheckOutTask(CanUpdateDevice canUpdateDevice) {
        super(canUpdateDevice);
    }

    public Single<Result> run(String checkOutBy) {
        this.checkOutBy = checkOutBy;
        return run();
    }

    @Override
    protected void task(final SingleEmitter<Result> emitter) {
        DatabaseReference deviceReference = FirebaseDatabase.getInstance().getReference()
                .child(TABLE_DEVICES)
                .child(DeviceUtil.getSerialNumber());
        deviceReference.child(DatabaseContract.DeviceColumns.CHECKED_OUT_BY)
                .setValue(checkOutBy);
        deviceReference.child(DatabaseContract.DeviceColumns.CHECK_OUT_TIME)
                .setValue(DatetimeUtil.getCurrentTimeInMillis());
        emitter.onSuccess(Result.success());
    }
}
