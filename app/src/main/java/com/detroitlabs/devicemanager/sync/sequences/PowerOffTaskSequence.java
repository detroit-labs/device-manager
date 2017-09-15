package com.detroitlabs.devicemanager.sync.sequences;


import com.detroitlabs.devicemanager.sync.Result;
import com.detroitlabs.devicemanager.sync.tasks.UpdateBatteryTask;

import javax.inject.Inject;

import io.reactivex.Single;

public final class PowerOffTaskSequence extends AsyncTaskSequence<Result> {

    private final UpdateBatteryTask updateBatteryTask;

    @Inject
    PowerOffTaskSequence(UpdateBatteryTask updateBatteryTask) {
        this.updateBatteryTask = updateBatteryTask;
    }

    @Override
    public Single<Result> run() {
        return updateBatteryTask.run();
    }
}
