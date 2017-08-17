package com.detroitlabs.devicemanager.sync;


import android.util.Log;

import com.detroitlabs.devicemanager.sync.tasks.FirebaseAuthTask;
import com.detroitlabs.devicemanager.sync.tasks.RequestDomainPermissionTask;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public class DomainSignInSequence extends AsyncTaskSequence<SignInResult> {

    private static final String TAG = DomainSignInSequence.class.getName();
    private final RequestDomainPermissionTask requestDomainPermissionTask;
    private final FirebaseAuthTask firebaseAuthTask;

    public DomainSignInSequence(RequestDomainPermissionTask requestDomainPermissionTask,
                                FirebaseAuthTask firebaseAuthTask) {

        this.requestDomainPermissionTask = requestDomainPermissionTask;
        this.firebaseAuthTask = firebaseAuthTask;
    }

    @Override
    public Single<SignInResult> run() {

        return requestDomainPermissionTask.run()
                .flatMap(firebaseAuth())
                .flatMap();
    }

    private Function<GoogleSignInResult, Single<SignInResult>> firebaseAuth() {
        return new Function<GoogleSignInResult, Single<SignInResult>>() {
            @Override
            public Single<SignInResult> apply(@NonNull GoogleSignInResult result) throws Exception {
                if (result.isSuccess()) {
                    Log.d(TAG, "user logged in with a detroit labs account");
                    return firebaseAuthTask.run(result.getSignInAccount().getIdToken());
                } else {
                    Log.d(TAG, "User failed to login with a detroit labs account");
                    Log.d(TAG, "status code: " + result.getStatus().getStatusCode());
                    return Single.just(SignInResult.empty());
                }
            }
        };
    }
}
