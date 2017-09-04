package com.detroitlabs.devicemanager.sync.tasks;


import com.detroitlabs.devicemanager.DmApplication;
import com.detroitlabs.devicemanager.repository.DeviceRepository;
import com.detroitlabs.devicemanager.sync.Result;

import javax.inject.Inject;

import io.reactivex.SingleEmitter;
import io.reactivex.functions.Consumer;

public class LocalRegisterTask extends AsyncTask<Result> {

    private final DeviceRepository deviceRepo;

    @Inject
    public LocalRegisterTask(DeviceRepository deviceRepo) {
        this.deviceRepo = deviceRepo;
    }

    @Override
    protected void task(final SingleEmitter<Result> emitter) {
        deviceRepo.insert(DmApplication.getThisDevice())
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
