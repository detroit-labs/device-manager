package com.detroitlabs.devicemanager.sync.tasks;


import android.content.Intent;
import android.util.Log;

import com.detroitlabs.devicemanager.sync.Ui;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class PromptForPermissionTask extends AsyncTask<GoogleSignInResult> {
    private static final int REQUEST_GOOGLE_SING_IN = 223;
    private static final String TAG = PromptForPermissionTask.class.getName();

    private final GoogleApiClient googleApiClient;

    private final Ui ui;

    @Inject
    PromptForPermissionTask(GoogleApiClient googleApiClient, Ui ui) {
        this.googleApiClient = googleApiClient;
        this.ui = ui;
    }

    @Override
    protected void task(final SingleEmitter<GoogleSignInResult> emitter) {
        Log.d(TAG, "Start prompting for permission to google account");
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        ui.startActivityForResult(signInIntent, REQUEST_GOOGLE_SING_IN, new Ui.ActivityResult() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                emitter.onSuccess(result);
            }
        });
    }

    @Override
    public Single<GoogleSignInResult> run() {
        return super.runOn(AndroidSchedulers.mainThread());
    }
}
