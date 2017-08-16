package com.detroitlabs.devicemanager.sync;


import android.util.Log;

import com.detroitlabs.devicemanager.sync.tasks.FirebaseAuthTask;
import com.detroitlabs.devicemanager.sync.tasks.PromptForPermissionTask;
import com.detroitlabs.devicemanager.sync.tasks.TestDeviceSilentSignInTask;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

public class TestDeviceSignInSequence extends AsyncTaskSequence<FirebaseUser> {
    private static final String TAG = TestDeviceSignInSequence.class.getName();
    private final TestDeviceSilentSignInTask silentSignInTask;
    private final PromptForPermissionTask promptForPermissionTask;
    private final FirebaseAuthTask firebaseAuthTask;

    @Inject
    public TestDeviceSignInSequence(TestDeviceSilentSignInTask silentSignInTask,
                                    PromptForPermissionTask promptForPermissionTask,
                                    FirebaseAuthTask firebaseAuthTask) {

        this.silentSignInTask = silentSignInTask;
        this.promptForPermissionTask = promptForPermissionTask;
        this.firebaseAuthTask = firebaseAuthTask;
    }

    @Override
    public Single<SignInResult> run() {
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
                    return Single.just(SignInResult.empty());
                }
            }
        };
    }
}
