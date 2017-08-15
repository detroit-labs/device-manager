package com.detroitlabs.devicemanager.sync.tasks;

import android.support.annotation.NonNull;
import android.util.Log;

import com.detroitlabs.devicemanager.sync.SignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;


public class FirebaseAuthTask extends AsyncTask<SignInResult> {

    private static final String TAG = FirebaseAuthTask.class.getName();
    private String idToken;

    @Inject
    public FirebaseAuthTask() {
    }

    public Single<SignInResult> run(String idToken) {
        this.idToken = idToken;
        return run();
    }

    @Override
    protected void task(final SingleEmitter<SignInResult> emitter) {
        Log.d(TAG, "start firebase sign in");
        FirebaseAuth auth = FirebaseAuth.getInstance();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Firebase sign in successful");
                            emitter.onSuccess(new SignInResult(task.getResult().getUser()));
                        } else {
                            Log.e(TAG, "Firebase sign in failed", task.getException());
                            emitter.onSuccess(new SignInResult(task.getException()));
                        }
                    }
                });
    }
}
