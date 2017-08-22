package com.detroitlabs.devicemanager.sync.sequences;

import com.detroitlabs.devicemanager.sync.tasks.GetUserTask;
import com.detroitlabs.devicemanager.sync.tasks.UpdateBatteryTask;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;


public final class PowerOnTaskSequence extends AsyncTaskSequence<Boolean> {

    private final UpdateBatteryTask updateBatteryTask;
    private final GetUserTask getUserTask;
    private final OwningNotificationSequence owningNotificationSequence;

    @Inject
    public PowerOnTaskSequence(UpdateBatteryTask updateBatteryTask,
                               GetUserTask getUserTask,
                               OwningNotificationSequence owningNotificationSequence) {
        this.updateBatteryTask = updateBatteryTask;
        this.getUserTask = getUserTask;
        this.owningNotificationSequence = owningNotificationSequence;
    }

    @Override
    public Single<Boolean> run() {

        return getUserTask.run()
                .flatMap(updateBattery())
                .flatMap(checkUser());
    }

    private Function<GetUserTask.Result, Single<GetUserTask.Result>> updateBattery() {
        return new Function<GetUserTask.Result, Single<GetUserTask.Result>>() {
            @Override
            public Single<GetUserTask.Result> apply(@NonNull GetUserTask.Result result) throws Exception {
                if (result.isSuccess()) {
                    updateBatteryTask.run().subscribe();
                }
                return Single.just(result);
            }
        };
    }

    private Function<GetUserTask.Result, Single<Boolean>> checkUser() {
        return new Function<GetUserTask.Result, Single<Boolean>>() {
            @Override
            public Single<Boolean> apply(@NonNull GetUserTask.Result result) throws Exception {
                if (result.isSuccess()) {
                    return owningNotificationSequence.run();
                } else {
                    return Single.just(false);
                }
            }
        };
    }
}
