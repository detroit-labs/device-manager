package com.detroitlabs.devicemanager.sync.tasks;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import io.reactivex.SingleEmitter;


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
