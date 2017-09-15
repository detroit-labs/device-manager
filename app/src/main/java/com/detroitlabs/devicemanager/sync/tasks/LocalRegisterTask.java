package com.detroitlabs.devicemanager.sync.tasks;


import android.content.Context;

import com.detroitlabs.devicemanager.di.qualifiers.ApplicationContext;
import com.detroitlabs.devicemanager.repository.DeviceRepository;
import com.detroitlabs.devicemanager.sync.Result;
import com.detroitlabs.devicemanager.utils.DeviceUtil;

import javax.inject.Inject;

import io.reactivex.SingleEmitter;
import io.reactivex.functions.Consumer;

public class LocalRegisterTask extends AsyncTask<Result> {

    private final Context context;
    private final DeviceRepository deviceRepo;

    @Inject
    public LocalRegisterTask(@ApplicationContext Context context,
                             DeviceRepository deviceRepo) {
        this.context = context;
        this.deviceRepo = deviceRepo;
    }

    @Override
    protected void task(final SingleEmitter<Result> emitter) {
        deviceRepo.insert(DeviceUtil.readThisDevice(context))
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean isSuccess) throws Exception {
                        if (isSuccess) {
                            emitter.onSuccess(Result.success());
                        } else {
                            emitter.onSuccess(Result.failure(new IllegalStateException("Failed to insert")));
                        }
                    }
                });
    }
}
