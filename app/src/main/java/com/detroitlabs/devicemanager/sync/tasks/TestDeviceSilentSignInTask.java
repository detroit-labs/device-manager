package com.detroitlabs.devicemanager.sync.tasks;


import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.SingleEmitter;

public class TestDeviceSilentSignInTask extends AsyncTask<GoogleSignInResult> {
    private static final String TAG = TestDeviceSilentSignInTask.class.getName();
    private final GoogleApiClient testDeviceGoogleApiClient;

    @Inject
    TestDeviceSilentSignInTask(GoogleApiClient googleApiClient) {
        this.testDeviceGoogleApiClient = googleApiClient;
    }

    @Override
    protected void task(final SingleEmitter<GoogleSignInResult> emitter) {
        Log.d(TAG, "Start silently sign in to google");
        OptionalPendingResult<GoogleSignInResult> opr =
                Auth.GoogleSignInApi.silentSignIn(testDeviceGoogleApiClient);
        if (opr.isDone()) {
            GoogleSignInResult result = opr.get();
            emitter.onSuccess(result);
        } else {
            Log.d(TAG, "has not signed in on this device");
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    emitter.onSuccess(googleSignInResult);
                }
            }, 5000, TimeUnit.MILLISECONDS);
        }
    }
}
