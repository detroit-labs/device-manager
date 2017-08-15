package com.detroitlabs.devicemanager.sync;


import android.util.Log;

import com.detroitlabs.devicemanager.sync.tasks.GetUserTask;
import com.detroitlabs.devicemanager.sync.tasks.DbSyncTask;
import com.detroitlabs.devicemanager.sync.tasks.RegisterTask;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public class InitialSyncSequence extends AsyncTaskSequence<Boolean> {
    private static final String TAG = InitialSyncSequence.class.getName();
    private final DbSyncTask dbSyncTask;

    private final GetUserTask getUserTask;

    private final RegisterTask registerTask;

    private final TestDeviceSignInSequence signInSequence;

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
                .flatMap(checkUser())
                .flatMap(register())
                .flatMap(syncDb());
    }

    private Function<GetUserTask.Result, Single<SignInResult>> checkUser() {
        return new Function<GetUserTask.Result, Single<SignInResult>>() {
            @Override
            public Single<SignInResult> apply(@NonNull GetUserTask.Result result) throws Exception {
                Log.d(TAG, "start checking getting user result");
                if (result.isSuccess()) {
                    return Single.just(new SignInResult(result.user));
                } else {
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
                    return dbSyncTask.run();
                } else {
                    return Single.just(false);
                }
            }
        };
    }


}
