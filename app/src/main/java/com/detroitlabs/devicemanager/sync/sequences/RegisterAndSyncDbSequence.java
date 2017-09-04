package com.detroitlabs.devicemanager.sync.sequences;

import android.util.Log;

import com.detroitlabs.devicemanager.sync.Result;
import com.detroitlabs.devicemanager.sync.tasks.DbSyncTask;
import com.detroitlabs.devicemanager.sync.tasks.GetRegistrableTask;
import com.detroitlabs.devicemanager.sync.tasks.LocalRegisterTask;
import com.detroitlabs.devicemanager.sync.tasks.RegisterTask;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;


public final class RegisterAndSyncDbSequence extends AsyncTaskSequence<Boolean> {

    private static final String TAG = RegisterAndSyncDbSequence.class.getName();
    private final GetRegistrableTask getRegistrableTask;
    private final RegisterTask registerTask;
    private final LocalRegisterTask localRegisterTask;
    private final DbSyncTask dbSyncTask;

    @Inject
    public RegisterAndSyncDbSequence(GetRegistrableTask getRegistrableTask,
                                     RegisterTask registerTask,
                                     LocalRegisterTask localRegisterTask,
                                     DbSyncTask dbSyncTask) {
        this.getRegistrableTask = getRegistrableTask;
        this.registerTask = registerTask;
        this.localRegisterTask = localRegisterTask;
        this.dbSyncTask = dbSyncTask;
    }

    @Override
    public Single<Boolean> run() {
        return getRegistrableTask.run()
                .flatMap(register())
                .flatMap(syncDb());
    }

    private Function<Boolean, SingleSource<Result>> register() {
        return new Function<Boolean, SingleSource<Result>>() {
            @Override
            public SingleSource<Result> apply(@NonNull Boolean isRegistrable) throws Exception {
                if (isRegistrable) {
                    Log.d(TAG, "Start firebase register");
                    return registerTask.run();
                } else {
                    Log.d(TAG, "device or account not registrable, start local register");
                    return localRegisterTask.run();
                }
            }
        };
    }

    private Function<Result, Single<Boolean>> syncDb() {
        return new Function<Result, Single<Boolean>>() {
            @Override
            public Single<Boolean> apply(@NonNull Result result) throws Exception {
                Log.d(TAG, "register result: " + result.isSuccess());
                updateStatus("Syncing Db");
                return dbSyncTask.run();
            }
        };
    }


}
