package com.detroitlabs.devicemanager.sync.sequences;

import com.detroitlabs.devicemanager.specification.HasGetSerialNumberPermission;
import com.detroitlabs.devicemanager.sync.Result;
import com.detroitlabs.devicemanager.sync.tasks.CheckInNotificationTask;
import com.detroitlabs.devicemanager.sync.tasks.CheckOutNotificationTask;
import com.detroitlabs.devicemanager.sync.tasks.GetOwnerTask;
import com.detroitlabs.devicemanager.sync.tasks.LogInNotificationTask;
import com.detroitlabs.devicemanager.sync.tasks.RequestPermissionNotificationTask;
import com.detroitlabs.devicemanager.utils.AccountUtil;
import com.detroitlabs.devicemanager.utils.DeviceUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * if device is not a real device, then do not show any notification
 * if no logged in user, then notify user to log in
 * if logged in with a non testing account, then do not show any notification
 * if os is OREO or higher and app does not have permission, then show request permission notification
 * otherwise, show device update notification
 */
public final class OwningNotificationSequence extends AsyncTaskSequence<Result> {

    private final GetOwnerTask getOwnerTask;
    private final LogInNotificationTask logInNotificationTask;
    private final HasGetSerialNumberPermission hasGetSerialNumberPermission;
    private final RequestPermissionNotificationTask requestPermissionNotificationTask;
    private final CheckOutNotificationTask checkOutNotificationTask;
    private final CheckInNotificationTask checkInNotificationTask;

    @Inject
    public OwningNotificationSequence(GetOwnerTask getOwnerTask,
                                      LogInNotificationTask logInNotificationTask,
                                      CheckInNotificationTask checkInNotificationTask,
                                      CheckOutNotificationTask checkOutNotificationTask,
                                      HasGetSerialNumberPermission hasGetSerialNumberPermission,
                                      RequestPermissionNotificationTask requestPermissionNotificationTask) {

        this.getOwnerTask = getOwnerTask;
        this.logInNotificationTask = logInNotificationTask;
        this.hasGetSerialNumberPermission = hasGetSerialNumberPermission;
        this.requestPermissionNotificationTask = requestPermissionNotificationTask;
        this.checkOutNotificationTask = checkOutNotificationTask;
        this.checkInNotificationTask = checkInNotificationTask;
    }

    @Override
    public Single<Result> run() {
        if (DeviceUtil.isEmulator()) {
            return fail("Not a real device");
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return logInNotificationTask.run();
        }

        if (AccountUtil.isTestAccount(user.getEmail())) {
            if (hasGetSerialNumberPermission.isSatisfied()) {
                return getOwnerTask.run()
                        .flatMap(checkOwner());
            } else {
                return requestPermissionNotificationTask.run();
            }
        }
        return fail("Not test account");

    }

    private Function<GetOwnerTask.Result, Single<Result>> checkOwner() {
        return new Function<GetOwnerTask.Result, Single<Result>>() {
            @Override
            public Single<Result> apply(@NonNull GetOwnerTask.Result result) throws Exception {
                if (result.isCheckedOut()) {
                    return checkInNotificationTask.run(result.owner);
                } else if (result.isSuccess()) {
                    return checkOutNotificationTask.run();
                } else {
                    return Single.just(Result.failure(result.exception));
                }
            }
        };
    }

    private Single<Result> fail(String msg) {
        return Single.just(Result.failure(new IllegalAccessException(msg)));
    }
}
