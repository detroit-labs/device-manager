package com.detroitlabs.devicemanager.sync.sequences;

import android.util.Log;

import com.detroitlabs.devicemanager.specification.CanUpdateDevice;
import com.detroitlabs.devicemanager.sync.Result;
import com.detroitlabs.devicemanager.sync.tasks.UpdateBatteryTask;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;


public final class PowerOnTaskSequence extends AsyncTaskSequence<Result> {

    private static final String TAG = PowerOnTaskSequence.class.getName();
    private final UpdateBatteryTask updateBatteryTask;
    private final CanUpdateDevice canUpdateDevice;
    private final OwningNotificationSequence owningNotificationSequence;

    @Inject
    public PowerOnTaskSequence(UpdateBatteryTask updateBatteryTask,
                               CanUpdateDevice canUpdateDevice,
                               OwningNotificationSequence owningNotificationSequence) {
        this.updateBatteryTask = updateBatteryTask;
        this.canUpdateDevice = canUpdateDevice;
        this.owningNotificationSequence = owningNotificationSequence;
    }

    @Override
    public Single<Result> run() {
        if (!canUpdateDevice.isSatisfied()) {
            Log.d(TAG, "not allow to update device");
            return Single.just(Result.failure(new IllegalAccessException("Not allow to update device")));
        }
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
