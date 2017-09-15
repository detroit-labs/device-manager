package com.detroitlabs.devicemanager.sync.tasks;


import com.detroitlabs.devicemanager.specification.CanUpdateDevice;
import com.detroitlabs.devicemanager.sync.Result;

import io.reactivex.SingleEmitter;

abstract class SyncDeviceTask extends AsyncTask<Result> {
    private final CanUpdateDevice canUpdateDevice;

    protected SyncDeviceTask(CanUpdateDevice canUpdateDevice) {
        this.canUpdateDevice = canUpdateDevice;
    }

    @Override
    protected void performTask(SingleEmitter<Result> e) {
        if (canUpdateDevice.isSatisfied()) {
            super.performTask(e);
        } else {
            e.onSuccess(Result.failure(new IllegalAccessException("Not allow to sync this device with cloud")));
        }
    }
}
