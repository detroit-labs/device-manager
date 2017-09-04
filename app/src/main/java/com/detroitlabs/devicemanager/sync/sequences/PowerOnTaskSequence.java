package com.detroitlabs.devicemanager.sync.sequences;

import android.util.Log;

import com.detroitlabs.devicemanager.sync.tasks.GetRegistrableTask;
import com.detroitlabs.devicemanager.sync.tasks.UpdateBatteryTask;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;


public final class PowerOnTaskSequence extends AsyncTaskSequence<Boolean> {

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
    public Single<Boolean> run() {
        return getRegistrable.run()
                .flatMap(updateBattery());
    }

    private Function<Boolean, Single<Boolean>> updateBattery() {
        return new Function<Boolean, Single<Boolean>>() {
            @Override
            public Single<Boolean> apply(@NonNull Boolean isRegistrable) throws Exception {
                if (isRegistrable) {
                    Log.d(TAG, "Start updating battery percentage");
                    return updateBatteryTask.run().zipWith(Single.just(true), new BiFunction<Boolean, Boolean, Boolean>() {
                        @Override
                        public Boolean apply(@NonNull Boolean updateBatteryResult, @NonNull Boolean sendNotificationResult) throws Exception {
                            return updateBatteryResult && sendNotificationResult;
                        }
                    });
                } else {
                    return Single.just(false);
                }
            }
        };
    }
}
