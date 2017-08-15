package com.detroitlabs.devicemanager.sync.tasks;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;


public class GetUserTask extends AsyncTask<GetUserTask.Result> {

    private static final String TAG = GetUserTask.class.getName();

    @Inject
    public GetUserTask() {
    }

    @Override
    protected void task(SingleEmitter<Result> emitter) {
        emitter.onSuccess(new Result(FirebaseAuth.getInstance().getCurrentUser()));
    }

    public static class Result extends AsyncTask.Result{
        public FirebaseUser user;

        public Result(FirebaseUser user) {
            this.user = user;
        }

        public boolean isSuccess() {
            return user != null;
        }
    }
}
