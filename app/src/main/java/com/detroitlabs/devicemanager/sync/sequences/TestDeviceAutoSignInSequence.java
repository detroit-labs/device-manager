package com.detroitlabs.devicemanager.sync.sequences;


import android.util.Log;

import com.detroitlabs.devicemanager.sync.SignInResult;
import com.detroitlabs.devicemanager.sync.tasks.FirebaseAuthTask;
import com.detroitlabs.devicemanager.sync.tasks.GetUserTask;
import com.detroitlabs.devicemanager.sync.tasks.RequestAccountPermissionTask;
import com.detroitlabs.devicemanager.sync.tasks.TestDeviceSilentSignInTask;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public final class TestDeviceAutoSignInSequence extends AsyncTaskSequence<SignInResult> {
    private static final String TAG = TestDeviceAutoSignInSequence.class.getName();
    private final GetUserTask getUserTask;
    private final TestDeviceSilentSignInTask silentSignInTask;
    private final RequestAccountPermissionTask promptForPermissionTask;
    private final FirebaseAuthTask firebaseAuthTask;

    @Inject
    public TestDeviceAutoSignInSequence(GetUserTask getUserTask,
                                        TestDeviceSilentSignInTask silentSignInTask,
                                        RequestAccountPermissionTask promptForPermissionTask,
                                        FirebaseAuthTask firebaseAuthTask) {
        this.getUserTask = getUserTask;
        this.silentSignInTask = silentSignInTask;
        this.promptForPermissionTask = promptForPermissionTask;
        this.firebaseAuthTask = firebaseAuthTask;
    }

    @Override
    public Single<SignInResult> run() {
        return getUserTask.run()
                .flatMap(checkUser());

    }

    private Function<GetUserTask.Result, Single<SignInResult>> checkUser() {
        return new Function<GetUserTask.Result, Single<SignInResult>>() {
            @Override
            public Single<SignInResult> apply(@NonNull GetUserTask.Result result) throws Exception {
                Log.d(TAG, "start checking getting user result");
                if (result.isSuccess()) {
                    updateStatus("Already logged in");
                    return Single.just(SignInResult.success(result.user));
                } else {
                    updateStatus("Authenticating...");
                    return performSignIn();
                }
            }
        };
    }

    private Single<SignInResult> performSignIn() {
        return silentSignInTask.run()
                .flatMap(prompt())
                .flatMap(firebaseAuth());
    }

    private Function<GoogleSignInResult, SingleSource<GoogleSignInResult>> prompt() {
        return new Function<GoogleSignInResult, SingleSource<GoogleSignInResult>>() {
            @Override
            public SingleSource<GoogleSignInResult> apply(GoogleSignInResult result) throws Exception {
                if (!result.isSuccess()) {
                    Log.d(TAG, "silently sign in failed, try prompt for permission");
                    Log.d(TAG, "status code: " + result.getStatus().getStatusCode());
                    return promptForPermissionTask.run();
                } else {
                    Log.d(TAG, "silently sign in successfully, move to next");
                    return Single.just(result);
                }
            }
        };
    }

    private Function<GoogleSignInResult, SingleSource<SignInResult>> firebaseAuth() {
        return new Function<GoogleSignInResult, SingleSource<SignInResult>>() {
            @Override
            public SingleSource<SignInResult> apply(GoogleSignInResult result) throws Exception {
                if (result.isSuccess()) {
                    Log.d(TAG, "User accept google sign-in, now start firebase auth");
                    return firebaseAuthTask.run(result.getSignInAccount().getIdToken());
                } else {
                    Log.d(TAG, "User decline google sign-in, go to home screen");
                    Log.d(TAG, "status code: " + result.getStatus().getStatusCode());
                    return Single.just(SignInResult.failure(null));
                }
            }
        };
    }
}
