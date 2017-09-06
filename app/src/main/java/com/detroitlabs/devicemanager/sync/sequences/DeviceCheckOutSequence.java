package com.detroitlabs.devicemanager.sync.sequences;

import android.util.Log;

import com.detroitlabs.devicemanager.sync.Result;
import com.detroitlabs.devicemanager.sync.tasks.DeviceCheckOutTask;
import com.detroitlabs.devicemanager.sync.tasks.GetRegistrableTask;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;


public final class DeviceCheckOutSequence extends AsyncTaskSequence<Result> {
    private static final String TAG = DeviceCheckOutSequence.class.getName();
    private final GetRegistrableTask getRegistrableTask;
    private final DeviceCheckOutTask checkOutTask;
    private String checkOutBy;

    @Inject
    public DeviceCheckOutSequence(GetRegistrableTask getRegistrableTask,
                                  DeviceCheckOutTask checkOutTask) {

        this.getRegistrableTask = getRegistrableTask;
        this.checkOutTask = checkOutTask;
    }

    public Single<Result> run(String checkOutBy) {
        this.checkOutBy = checkOutBy;
        return run();
    }

    @Override
    public Single<Result> run() {
        if (checkOutBy == null) {
            return Single.error(new IllegalArgumentException("Please use run(String checkOutBy) and pass the parameter"));
        }
        return getRegistrableTask.run()
                .flatMap(checkout());
    }

    private Function<Boolean, Single<Result>> checkout() {
        return new Function<Boolean, Single<Result>>() {
            @Override
            public Single<Result> apply(@NonNull Boolean isRegistrable) throws Exception {
                if (isRegistrable) {
                    Log.d(TAG, "Device registrable, start check out");
                    return checkOutTask.run(checkOutBy);
                } else {
                    Log.d(TAG, "Device not registrable");
                    return Single.just(Result.failure(new IllegalStateException("Device not registrable")));
                }
            }
        };
    }
}
