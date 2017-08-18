package com.detroitlabs.devicemanager.sync.sequences;


import android.util.Log;

import com.detroitlabs.devicemanager.sync.SignInResult;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public final class ManualSignInSyncSequence extends AsyncTaskSequence<Boolean> {

    private static final String TAG = ManualSignInSyncSequence.class.getName();
    private final ManualSignInSequence manualSignInSequence;
    private final RegisterAndSyncDbSequence registerAndSyncDbSequence;

    @Inject
    public ManualSignInSyncSequence(ManualSignInSequence manualSignInSequence,
                                    RegisterAndSyncDbSequence registerAndSyncDbSequence) {
        this.manualSignInSequence = manualSignInSequence;
        this.registerAndSyncDbSequence = registerAndSyncDbSequence;
    }

    @Override
    public Single<Boolean> run() {
        updateStatus("Authenticating...");
        return manualSignInSequence.run()
                .flatMap(registerAndSync());
    }

    @Override
    public Observable<String> status() {
        return Observable.merge(statusSubject, registerAndSyncDbSequence.status());
    }

    private Function<SignInResult, Single<Boolean>> registerAndSync() {
        return new Function<SignInResult, Single<Boolean>>() {
            @Override
            public Single<Boolean> apply(@NonNull SignInResult result) throws Exception {
                if (result.isSuccess()) {
                    Log.d(TAG, "user logged in with a detroit labs account");
                    return registerAndSyncDbSequence.run();
                } else {
                    Log.e(TAG, "User failed to login with a detroit labs account", result.exception);
                    return Single.just(false);
                }
            }
        };
    }
}
