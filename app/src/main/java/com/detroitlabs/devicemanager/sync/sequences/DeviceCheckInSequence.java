package com.detroitlabs.devicemanager.sync.sequences;

import com.detroitlabs.devicemanager.sync.Result;
import com.detroitlabs.devicemanager.sync.tasks.DeviceCheckInTask;
import com.detroitlabs.devicemanager.sync.tasks.GetUserTask;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;


public class DeviceCheckInSequence extends AsyncTaskSequence<Result> {
    private final GetUserTask getUserTask;
    private final DeviceCheckInTask checkInTask;

    @Inject
    public DeviceCheckInSequence(GetUserTask getUserTask,
                                 DeviceCheckInTask checkInTask) {

        this.getUserTask = getUserTask;
        this.checkInTask = checkInTask;
    }


    @Override
    public Single<Result> run() {
        return getUserTask.run()
                .flatMap(checkIn());
    }

    private Function<Result, Single<Result>> checkIn() {
        return new Function<Result, Single<Result>>() {
            @Override
            public Single<Result> apply(@NonNull Result result) throws Exception {
                if (result.isSuccess()) {
                    return checkInTask.run();
                } else {
                    return Single.just(result);
                }
            }
        };
    }
}
