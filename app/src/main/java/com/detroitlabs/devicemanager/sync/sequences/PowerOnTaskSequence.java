package com.detroitlabs.devicemanager.sync.sequences;

import android.util.Log;

import com.detroitlabs.devicemanager.sync.Result;
import com.detroitlabs.devicemanager.sync.tasks.GetRegistrableTask;
import com.detroitlabs.devicemanager.sync.tasks.UpdateBatteryTask;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;


public final class PowerOnTaskSequence extends AsyncTaskSequence<Result> {

    private static final String TAG = PowerOnTaskSequence.class.getName();
    private final UpdateBatteryTask updateBatteryTask;
    private final GetRegistrableTask getRegistrable;
    private final OwningNotificationSequence owningNotificationSequence;

    @Inject
    public PowerOnTaskSequence(UpdateBatteryTask updateBatteryTask,
                               GetRegistrableTask getRegistrableTask,
                               OwningNotificationSequence owningNotificationSequence) {
        this.updateBatteryTask = updateBatteryTask;
        this.getRegistrable = getRegistrableTask;
        this.owningNotificationSequence = owningNotificationSequence;
    }

    @Override
    public Single<Result> run() {
        return getRegistrable.run()
                .flatMap(updateBattery());
    }

    private Function<Boolean, Single<Result>> updateBattery() {
        return new Function<Boolean, Single<Result>>() {
            @Override
            public Single<Result> apply(@NonNull Boolean isRegistrable) throws Exception {
                if (isRegistrable) {
                    Log.d(TAG, "Start updating battery percentage");
                    return updateBatteryTask.run().zipWith(owningNotificationSequence.run(),
                            new BiFunction<Result, Result, Result>() {
                                @Override
                                public Result apply(@NonNull Result updateBatteryResult,
                                                    @NonNull Result sendNotificationResult) throws Exception {
                                    if (!updateBatteryResult.isSuccess()) {
                                        return updateBatteryResult;
                                    } else if (!sendNotificationResult.isSuccess()) {
                                        return sendNotificationResult;
                                    } else {
                                        return Result.success();
                                    }
                                }
                            });
                } else {
                    return Single.just(Result.failure(new IllegalStateException("Device not registrable")));
                }
            }
        };
    }
}
