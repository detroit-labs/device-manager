package com.detroitlabs.devicemanager.sync.sequences;


import com.detroitlabs.devicemanager.sync.Result;
import com.detroitlabs.devicemanager.sync.tasks.GetRegistrableTask;
import com.detroitlabs.devicemanager.sync.tasks.UpdateBatteryTask;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public final class PowerOffTaskSequence extends AsyncTaskSequence<Result> {

    private final UpdateBatteryTask updateBatteryTask;
    private final GetRegistrableTask getRegistrableTask;

    @Inject
    PowerOffTaskSequence(UpdateBatteryTask updateBatteryTask,
                         GetRegistrableTask getRegistrableTask) {
        this.updateBatteryTask = updateBatteryTask;
        this.getRegistrableTask = getRegistrableTask;
    }

    @Override
    public Single<Result> run() {
        return getRegistrableTask.run()
                .flatMap(updateBattery());
    }

    private Function<Boolean, Single<Result>> updateBattery() {
        return new Function<Boolean, Single<Result>>() {
            @Override
            public Single<Result> apply(@NonNull Boolean isRegistrable) throws Exception {
                if (isRegistrable) {
                    return updateBatteryTask.run();
                } else {
                    return Single.just(Result.failure(new IllegalStateException("Device not registrable")));
                }
            }
        };
    }
}
