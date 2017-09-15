package com.detroitlabs.devicemanager.sync.tasks;


import com.detroitlabs.devicemanager.data.DatabaseContract;
import com.detroitlabs.devicemanager.specification.CanUpdateDevice;
import com.detroitlabs.devicemanager.sync.Result;
import com.detroitlabs.devicemanager.utils.DeviceUtil;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;

import static com.detroitlabs.devicemanager.data.DatabaseContract.TABLE_DEVICES;

public class DeviceCheckOutTask extends SyncDeviceTask {
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
        FirebaseDatabase.getInstance().getReference()
                .child(TABLE_DEVICES)
                .child(DeviceUtil.getSerialNumber())
                .child(DatabaseContract.DeviceColumns.CHECKED_OUT_BY)
                .setValue(checkOutBy, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            emitter.onSuccess(Result.success());
                        } else {
                            emitter.onSuccess(Result.failure(databaseError.toException()));
                        }
                    }
                });
    }
}
