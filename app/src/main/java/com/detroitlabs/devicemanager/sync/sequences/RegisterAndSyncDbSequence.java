package com.detroitlabs.devicemanager.sync.sequences;

import android.util.Log;

import com.detroitlabs.devicemanager.specification.CanUpdateDevice;
import com.detroitlabs.devicemanager.sync.Result;
import com.detroitlabs.devicemanager.sync.tasks.DbSyncTask;
import com.detroitlabs.devicemanager.sync.tasks.LocalRegisterTask;
import com.detroitlabs.devicemanager.sync.tasks.RegisterTask;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;


public final class RegisterAndSyncDbSequence extends AsyncTaskSequence<Result> {

    private static final String TAG = RegisterAndSyncDbSequence.class.getName();
    private final RegisterTask registerTask;
    private final LocalRegisterTask localRegisterTask;
    private final DbSyncTask dbSyncTask;
    private final CanUpdateDevice canUpdateDevice;

    @Inject
    public RegisterAndSyncDbSequence(CanUpdateDevice canUpdateDevice,
                                     RegisterTask registerTask,
                                     LocalRegisterTask localRegisterTask,
                                     DbSyncTask dbSyncTask) {
        this.canUpdateDevice = canUpdateDevice;
        this.registerTask = registerTask;
        this.localRegisterTask = localRegisterTask;
        this.dbSyncTask = dbSyncTask;
    }

    @Override
    public Single<Result> run() {
        if (canUpdateDevice.isSatisfied()) {
            Log.d(TAG, "Start firebase register");
            return registerTask.run()
                    .flatMap(syncDb());
        } else {
            Log.d(TAG, "device or account not registrable, start local register");
            return localRegisterTask.run();
        }
    }

    private Function<Boolean, SingleSource<Result>> register() {
        return new Function<Boolean, SingleSource<Result>>() {
            @Override
            public SingleSource<Result> apply(@NonNull Boolean isRegistrable) throws Exception {
                if (isRegistrable) {
                    return registerTask.run();
                } else {
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
