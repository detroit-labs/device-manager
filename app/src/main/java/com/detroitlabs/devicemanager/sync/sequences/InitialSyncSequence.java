package com.detroitlabs.devicemanager.sync.sequences;


import android.util.Log;

import com.detroitlabs.devicemanager.sync.Result;
import com.detroitlabs.devicemanager.sync.SignInResult;
import com.detroitlabs.devicemanager.sync.tasks.CheckReadPhoneStatePermissionTask;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public final class InitialSyncSequence extends AsyncTaskSequence<Result> {
    private static final String TAG = InitialSyncSequence.class.getName();

    private final TestDeviceAutoSignInSequence autoSignInSequence;

    private final CheckReadPhoneStatePermissionTask checkReadPhoneStatePermissionTask;
    private final RegisterAndSyncDbSequence registerAndSyncDbSequence;

    @Inject
    public InitialSyncSequence(CheckReadPhoneStatePermissionTask checkReadPhoneStatePermissionTask,
                               RegisterAndSyncDbSequence registerAndSyncDbSequence,
                               TestDeviceAutoSignInSequence autoSignInSequence) {
        this.checkReadPhoneStatePermissionTask = checkReadPhoneStatePermissionTask;
        this.registerAndSyncDbSequence = registerAndSyncDbSequence;
        this.autoSignInSequence = autoSignInSequence;
    }

    @Override
    public Single<Result> run() {
        return checkReadPhoneStatePermissionTask.run()
                .flatMap(signIn())
                .flatMap(registerAndSync());
    }

    private Function<String, Single<SignInResult>> signIn() {
        return new Function<String, Single<SignInResult>>() {
            @Override
            public Single<SignInResult> apply(@NonNull String serialNumber) throws Exception {
                return autoSignInSequence.run();
            }
        };
    }

    public Observable<String> status() {
        return Observable.merge(statusSubject, registerAndSyncDbSequence.status(), autoSignInSequence.status());
    }

    private Function<SignInResult, Single<Result>> registerAndSync() {
        return new Function<SignInResult, Single<Result>>() {
            @Override
            public Single<Result> apply(@NonNull SignInResult result) throws Exception {
                if (result.isSuccess()) {
                    Log.d(TAG, "authentication successful. start register and sync");
                    updateStatus("Authentication successful!");
                    return registerAndSyncDbSequence.run();
                } else {
                    return Single.just(Result.failure(result.exception));
                }
            }
        };
    }
}
