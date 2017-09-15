package com.detroitlabs.devicemanager.sync.sequences;

import android.util.Log;

import com.detroitlabs.devicemanager.sync.Result;
import com.detroitlabs.devicemanager.sync.tasks.DbSyncTask;
import com.detroitlabs.devicemanager.sync.tasks.LocalRegisterTask;
import com.detroitlabs.devicemanager.sync.tasks.RegisterTask;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;


public final class RegisterAndSyncDbSequence extends AsyncTaskSequence<Result> {

    private static final String TAG = RegisterAndSyncDbSequence.class.getName();
    private final RegisterTask registerTask;
    private final LocalRegisterTask localRegisterTask;
    private final DbSyncTask dbSyncTask;

    @Inject
    public RegisterAndSyncDbSequence(RegisterTask registerTask,
                                     LocalRegisterTask localRegisterTask,
                                     DbSyncTask dbSyncTask) {
        this.registerTask = registerTask;
        this.localRegisterTask = localRegisterTask;
        this.dbSyncTask = dbSyncTask;
    }

    @Override
    public Single<Result> run() {
        return registerTask.run()
                .flatMap(localRegister())
                .flatMap(syncDb());
    }

    private Function<Result, Single<Result>> localRegister() {
        return new Function<Result, Single<Result>>() {
            @Override
            public Single<Result> apply(@NonNull Result registerResult) throws Exception {
                if (registerResult.isSuccess()) {
                    Log.d(TAG, "Remote register succeeded");
                    return Single.just(registerResult);
                } else {
                    Log.d(TAG, "Remote register failed", registerResult.exception);
                    return localRegisterTask.run();
                }
            }
        };
    }


    private Function<Result, Single<Result>> syncDb() {
        return new Function<Result, Single<Result>>() {
            @Override
            public Single<Result> apply(@NonNull Result result) throws Exception {
                Log.d(TAG, "register result: " + result.isSuccess());
                updateStatus("Syncing Db");
                return dbSyncTask.run();
            }
        };
    }


}
