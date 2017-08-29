package com.detroitlabs.devicemanager.sync.sequences;

import com.detroitlabs.devicemanager.sync.Result;
import com.detroitlabs.devicemanager.sync.tasks.DeviceCheckOutTask;
import com.detroitlabs.devicemanager.sync.tasks.GetUserTask;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;


public class DeviceCheckOutSequence extends AsyncTaskSequence<Result> {
    private final GetUserTask getUserTask;
    private final DeviceCheckOutTask checkOutTask;
    private String checkOutBy;

    @Inject
    public DeviceCheckOutSequence(GetUserTask getUserTask,
                                  DeviceCheckOutTask checkOutTask) {

        this.getUserTask = getUserTask;
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
        return getUserTask.run()
                .flatMap(checkout());
    }

    private Function<Result, Single<Result>> checkout() {
        return new Function<Result, Single<Result>>() {
            @Override
            public Single<Result> apply(@NonNull Result result) throws Exception {
                if (result.isSuccess()) {
                    return checkOutTask.run(checkOutBy);
                } else {
                    return Single.just(Result.failure(result.exception));
                }
            }
        };
    }
}
