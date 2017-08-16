package com.detroitlabs.devicemanager.sync;


import android.util.Log;

import com.detroitlabs.devicemanager.sync.tasks.DbSyncTask;
import com.detroitlabs.devicemanager.sync.tasks.GetUserTask;
import com.detroitlabs.devicemanager.sync.tasks.RegisterTask;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;

public class InitialSyncSequence extends AsyncTaskSequence<Boolean> {
    private static final String TAG = InitialSyncSequence.class.getName();

    private final DbSyncTask dbSyncTask;
    private final GetUserTask getUserTask;
    private final RegisterTask registerTask;
    private final TestDeviceSignInSequence signInSequence;

    private final PublishSubject<String> statusSubject = PublishSubject.create();

    @Inject
    public InitialSyncSequence(DbSyncTask dbSyncTask,
                               GetUserTask getUserTask,
                               RegisterTask registerTask,
                               TestDeviceSignInSequence signInSequence) {
        this.dbSyncTask = dbSyncTask;
        this.getUserTask = getUserTask;
        this.registerTask = registerTask;
        this.signInSequence = signInSequence;
    }

    @Override
    public Single<Boolean> run() {
        return getUserTask.run()
                .compose(checkTheUser())
                .compose(doRegister())
                .compose(syncTheDb());
    }

    public Observable<String> status() {
        return statusSubject;
    }

    private SingleTransformer<GetUserTask.Result, SignInResult> checkTheUser() {
        return new SingleTransformer<GetUserTask.Result, SignInResult>() {
            @Override
            public Single<SignInResult> apply(@NonNull Single<GetUserTask.Result> upstream) {
                upstream.observeOn(AndroidSchedulers.mainThread());
                return upstream.flatMap(checkUser());
            }
        };
    }

    private SingleTransformer<SignInResult, RegisterTask.Result> doRegister() {
        return new SingleTransformer<SignInResult, RegisterTask.Result>() {
            @Override
            public Single<RegisterTask.Result> apply(@NonNull Single<SignInResult> upstream) {
                upstream.observeOn(AndroidSchedulers.mainThread());
                return upstream.flatMap(register());
            }
        };
    }

    private SingleTransformer<RegisterTask.Result, Boolean> syncTheDb() {
        return new SingleTransformer<RegisterTask.Result, Boolean>() {
            @Override
            public Single<Boolean> apply(@NonNull Single<RegisterTask.Result> upstream) {
                upstream.observeOn(AndroidSchedulers.mainThread());
                return upstream.flatMap(syncDb());
            }
        };
    }

    private Function<GetUserTask.Result, Single<SignInResult>> checkUser() {
        return new Function<GetUserTask.Result, Single<SignInResult>>() {
            @Override
            public Single<SignInResult> apply(@NonNull GetUserTask.Result result) throws Exception {
                Log.d(TAG, "start checking getting user result");
                if (result.isSuccess()) {
                    updateStatus("Found authenticated user");
                    return Single.just(new SignInResult(result.user));
                } else {
                    updateStatus("Authenticating...");
                    return signInSequence.run();
                }
            }
        };
    }

    private Function<SignInResult, Single<RegisterTask.Result>> register() {
        return new Function<SignInResult, Single<RegisterTask.Result>>() {
            @Override
            public Single<RegisterTask.Result> apply(@NonNull SignInResult result) throws Exception {
                if (result.isSuccess()) {
                    updateStatus("Authentication successful!");
                    return registerTask.run();
                } else {
                    return Single.just(RegisterTask.Result.error(result.exception));
                }
            }
        };
    }

    private Function<RegisterTask.Result, Single<Boolean>> syncDb() {
        return new Function<RegisterTask.Result, Single<Boolean>>() {
            @Override
            public Single<Boolean> apply(@NonNull RegisterTask.Result result) throws Exception {
                if (result.isSuccess()) {
                    updateStatus("Syncing database...");
                    return dbSyncTask.run();
                } else {
                    return Single.just(false);
                }
            }
        };
    }

    private void updateStatus(String status) {
        statusSubject.onNext(status);
    }
}
