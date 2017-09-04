package com.detroitlabs.devicemanager.sync.sequences;


import com.detroitlabs.devicemanager.sync.tasks.GetRegistrableTask;
import com.detroitlabs.devicemanager.sync.tasks.UpdateBatteryTask;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public final class PowerOffTaskSequence extends AsyncTaskSequence<Boolean> {

    private final UpdateBatteryTask updateBatteryTask;
    private final GetRegistrableTask getRegistrableTask;

    @Inject
    PowerOffTaskSequence(UpdateBatteryTask updateBatteryTask,
                         GetRegistrableTask getRegistrableTask) {
        this.updateBatteryTask = updateBatteryTask;
        this.getRegistrableTask = getRegistrableTask;
    }

    @Override
    public Single<Boolean> run() {
        return getRegistrableTask.run()
                .flatMap(updateBattery());
    }

    private Function<Boolean, Single<Boolean>> updateBattery() {
        return new Function<Boolean, Single<Boolean>>() {
            @Override
            public Single<Boolean> apply(@NonNull Boolean isRegistrable) throws Exception {
                if (isRegistrable) {
                    return updateBatteryTask.run();
                } else {
                    return Single.just(false);
                }
            }
        };
    }
}
