package com.detroitlabs.devicemanager.sync.sequences;

import android.util.Log;

import com.detroitlabs.devicemanager.sync.Result;
import com.detroitlabs.devicemanager.sync.tasks.UpdateBatteryTask;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;


public final class PowerOnTaskSequence extends AsyncTaskSequence<Result> {

    private static final String TAG = PowerOnTaskSequence.class.getName();
    private final UpdateBatteryTask updateBatteryTask;
    private final OwningNotificationSequence owningNotificationSequence;

    @Inject
    public PowerOnTaskSequence(UpdateBatteryTask updateBatteryTask,
                               OwningNotificationSequence owningNotificationSequence) {
        this.updateBatteryTask = updateBatteryTask;
        this.owningNotificationSequence = owningNotificationSequence;
    }

    @Override
    public Single<Result> run() {
        Log.d(TAG, "start to update battery and show notification");
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
    }
}
