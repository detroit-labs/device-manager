package com.detroitlabs.devicemanager.sync.sequences;


import com.detroitlabs.devicemanager.specification.CanUpdateDevice;
import com.detroitlabs.devicemanager.sync.Result;
import com.detroitlabs.devicemanager.sync.tasks.UpdateBatteryTask;

import javax.inject.Inject;

import io.reactivex.Single;

public final class PowerOffTaskSequence extends AsyncTaskSequence<Result> {

    private final CanUpdateDevice canUpdateDevice;
    private final UpdateBatteryTask updateBatteryTask;

    @Inject
    PowerOffTaskSequence(CanUpdateDevice canUpdateDevice,
                         UpdateBatteryTask updateBatteryTask) {
        this.canUpdateDevice = canUpdateDevice;
        this.updateBatteryTask = updateBatteryTask;
    }

    @Override
    public Single<Result> run() {
        if (!canUpdateDevice.isSatisfied()) {
            return Single.just(Result.failure(new IllegalAccessException("Not allow to update battery status")));
        }
        return updateBatteryTask.run();
    }
}
