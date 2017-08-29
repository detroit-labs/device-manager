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
        emitter.onSuccess(Result.success(FirebaseAuth.getInstance().getCurrentUser()));
    }

    public static class Result extends com.detroitlabs.devicemanager.sync.Result {
        public FirebaseUser user;

        protected Result(FirebaseUser user) {
            super(null);
            this.user = user;
        }

        public static Result success(FirebaseUser user) {
            return new Result(user);
        }

        public boolean isSuccess() {
            return super.isSuccess() && user != null;
        }
    }
}
