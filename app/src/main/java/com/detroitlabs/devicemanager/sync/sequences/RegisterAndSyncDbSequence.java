package com.detroitlabs.devicemanager.sync.sequences;

import android.util.Log;

import com.detroitlabs.devicemanager.sync.tasks.DbSyncTask;
import com.detroitlabs.devicemanager.sync.tasks.RegisterTask;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;


public final class RegisterAndSyncDbSequence extends AsyncTaskSequence<Boolean> {

    private static final String TAG = RegisterAndSyncDbSequence.class.getName();
    private final RegisterTask registerTask;
    private final DbSyncTask dbSyncTask;

    @Inject
    public RegisterAndSyncDbSequence(RegisterTask registerTask,
                                     DbSyncTask dbSyncTask) {

        this.registerTask = registerTask;
        this.dbSyncTask = dbSyncTask;
    }

    @Override
    public Single<Boolean> run() {
        return registerTask.run()
                .flatMap(syncDb());
    }

    private Function<RegisterTask.Result, Single<Boolean>> syncDb() {
        return new Function<RegisterTask.Result, Single<Boolean>>() {
            @Override
            public Single<Boolean> apply(@NonNull RegisterTask.Result result) throws Exception {
                if (result.isSuccess()) {
                    Log.d(TAG, "register successful. start syncing db");
                    updateStatus("Syncing Db");
                    return dbSyncTask.run();
                } else {
                    return Single.just(false);
                }
            }
        };
    }


}
