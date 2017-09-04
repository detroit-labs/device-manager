package com.detroitlabs.devicemanager.sync.sequences;

import com.detroitlabs.devicemanager.sync.tasks.CheckInNotificationTask;
import com.detroitlabs.devicemanager.sync.tasks.CheckOutNotificationTask;
import com.detroitlabs.devicemanager.sync.tasks.GetOwnerTask;
import com.detroitlabs.devicemanager.sync.tasks.GetRegistrableTask;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;


public final class OwningNotificationSequence extends AsyncTaskSequence<Boolean> {

    private final GetOwnerTask getOwnerTask;
    private final GetRegistrableTask getRegistrableTask;
    private final CheckOutNotificationTask checkOutNotificationTask;
    private final CheckInNotificationTask checkInNotificationTask;

    @Inject
    public OwningNotificationSequence(GetOwnerTask getOwnerTask,
                                      GetRegistrableTask getRegistrableTask,
                                      CheckOutNotificationTask checkOutNotificationTask,
                                      CheckInNotificationTask checkInNotificationTask) {

        this.getOwnerTask = getOwnerTask;
        this.getRegistrableTask = getRegistrableTask;
        this.checkOutNotificationTask = checkOutNotificationTask;
        this.checkInNotificationTask = checkInNotificationTask;
    }

    @Override
    public Single<Boolean> run() {
        return getRegistrableTask.run()
                .flatMap(checkUser())
                .flatMap(checkOwner());
    }

    private Function<Boolean, Single<GetOwnerTask.Result>> checkUser() {
        return new Function<Boolean, Single<GetOwnerTask.Result>>() {
            @Override
            public Single<GetOwnerTask.Result> apply(@NonNull Boolean isRegistrable) throws Exception {
                if (isRegistrable) {
                    return getOwnerTask.run();
                } else {
                    return Single.just(GetOwnerTask.Result.failure(new IllegalStateException("Not allow to check in/out this device")));
                }
            }
        };
    }

    private Function<GetOwnerTask.Result, Single<Boolean>> checkOwner() {
        return new Function<GetOwnerTask.Result, Single<Boolean>>() {
            @Override
            public Single<Boolean> apply(@NonNull GetOwnerTask.Result result) throws Exception {
                if (result.isCheckedOut()) {
                    return checkInNotificationTask.run();
                } else {
                    return checkOutNotificationTask.run();
                }
            }
        };
    }
}
