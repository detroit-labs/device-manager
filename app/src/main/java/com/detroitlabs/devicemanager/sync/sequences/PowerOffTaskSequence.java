package com.detroitlabs.devicemanager.sync.sequences;


import com.detroitlabs.devicemanager.sync.tasks.GetUserTask;
import com.detroitlabs.devicemanager.sync.tasks.UpdateBatteryTask;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public final class PowerOffTaskSequence extends AsyncTaskSequence<Boolean> {

    private final UpdateBatteryTask updateBatteryTask;
    private final GetUserTask getUserTask;

    @Inject
    PowerOffTaskSequence(UpdateBatteryTask updateBatteryTask,
                         GetUserTask getUserTask) {
        this.updateBatteryTask = updateBatteryTask;
        this.getUserTask = getUserTask;
    }

    @Override
    public Single<Boolean> run() {
        return getUserTask.run()
                .flatMap(updateBattery());
    }

    private Function<GetUserTask.Result, Single<Boolean>> updateBattery() {
        return new Function<GetUserTask.Result, Single<Boolean>>() {
            @Override
            public Single<Boolean> apply(@NonNull GetUserTask.Result result) throws Exception {
                if (result.isSuccess()) {
                    return updateBatteryTask.run();
                } else {
                    return Single.just(false);
                }
            }
        };
    }
}
