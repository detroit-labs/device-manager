package com.detroitlabs.devicemanager.sync.sequences;


import android.util.Log;

import com.detroitlabs.devicemanager.sync.SignInResult;
import com.detroitlabs.devicemanager.sync.tasks.FirebaseAuthTask;
import com.detroitlabs.devicemanager.sync.tasks.RequestDomainPermissionTask;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public final class ManualSignInSequence extends AsyncTaskSequence<SignInResult> {

    private static final String TAG = ManualSignInSequence.class.getName();
    private final RequestDomainPermissionTask requestDomainPermissionTask;
    private final FirebaseAuthTask firebaseAuthTask;

    @Inject
    public ManualSignInSequence(RequestDomainPermissionTask requestDomainPermissionTask,
                                FirebaseAuthTask firebaseAuthTask) {

        this.requestDomainPermissionTask = requestDomainPermissionTask;
        this.firebaseAuthTask = firebaseAuthTask;
    }

    @Override
    public Single<SignInResult> run() {
        return requestDomainPermissionTask.run()
                .flatMap(firebaseAuth());
    }

    private Function<GoogleSignInResult, Single<SignInResult>> firebaseAuth() {
        return new Function<GoogleSignInResult, Single<SignInResult>>() {
            @Override
            public Single<SignInResult> apply(@NonNull GoogleSignInResult result) throws Exception {
                if (result.isSuccess()) {
                    Log.d(TAG, "User accept google sign-in, now start firebase auth");
                    return firebaseAuthTask.run(result.getSignInAccount().getIdToken());
                } else {
                    Log.d(TAG, "User decline google sign-in, go to home screen");
                    Log.d(TAG, "status code: " + result.getStatus().getStatusCode());
                    Log.d(TAG, result.getStatus().getStatusMessage());
                    return Single.just(SignInResult.failure(null));
                }
            }
        };
    }
}
