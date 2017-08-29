package com.detroitlabs.devicemanager.sync.sequences;

import com.detroitlabs.devicemanager.sync.Result;
import com.detroitlabs.devicemanager.sync.tasks.CheckInNotificationTask;
import com.detroitlabs.devicemanager.sync.tasks.CheckOutNotificationTask;
import com.detroitlabs.devicemanager.sync.tasks.GetOwnerTask;
import com.detroitlabs.devicemanager.sync.tasks.GetUserTask;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;


public final class OwningNotificationSequence extends AsyncTaskSequence<Boolean> {

    private final GetOwnerTask getOwnerTask;
    private final GetUserTask getUserTask;
    private final CheckOutNotificationTask checkOutNotificationTask;
    private final CheckInNotificationTask checkInNotificationTask;

    @Inject
    public OwningNotificationSequence(GetOwnerTask getOwnerTask,
                                      GetUserTask getUserTask,
                                      CheckOutNotificationTask checkOutNotificationTask,
                                      CheckInNotificationTask checkInNotificationTask) {

        this.getOwnerTask = getOwnerTask;
        this.getUserTask = getUserTask;
        this.checkOutNotificationTask = checkOutNotificationTask;
        this.checkInNotificationTask = checkInNotificationTask;
    }

    @Override
    public Single<Boolean> run() {
        return getUserTask.run()
                .flatMap(checkUser())
                .flatMap(checkOwner());
    }

    private Function<Result, Single<? extends Result>> checkUser() {
        return new Function<Result, Single<? extends Result>>() {
            @Override
            public Single<? extends Result> apply(@NonNull Result result) throws Exception {
                if (result.isSuccess()) {
                    return getOwnerTask.run();
                } else {
                    return Single.just(Result.failure(result.exception));
                }
            }
        };
    }

    private Function<Result, Single<Boolean>> checkOwner() {
        return new Function<Result, Single<Boolean>>() {
            @Override
            public Single<Boolean> apply(@NonNull Result result) throws Exception {
                if (result.isSuccess()) {
                    return checkInNotificationTask.run();
                } else {
                    return checkOutNotificationTask.run();
                }
            }
        };
    }
}
