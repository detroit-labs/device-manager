package com.detroitlabs.devicemanager.sync.tasks;

import com.detroitlabs.devicemanager.db.Device;
import com.detroitlabs.devicemanager.repository.DeviceRepository;

import javax.inject.Inject;

import io.reactivex.SingleEmitter;
import io.reactivex.functions.Consumer;


public class GetOwnerTask extends AsyncTask<GetOwnerTask.Result> {


    private final DeviceRepository deviceRepo;

    @Inject
    public GetOwnerTask(DeviceRepository deviceRepo) {
        this.deviceRepo = deviceRepo;
    }

    @Override
    protected void task(final SingleEmitter<GetOwnerTask.Result> emitter) {
        deviceRepo.getSelfDeviceSingle().subscribe(new Consumer<Device>() {
            @Override
            public void accept(Device device) throws Exception {
                emitter.onSuccess(Result.success(device.checkedOutBy));
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                emitter.onError(throwable);
            }
        });
    }

    public static class Result extends com.detroitlabs.devicemanager.sync.Result {
        public final String owner;

        protected Result(String owner) {
            super(null);
            this.owner = owner;
        }

        protected Result(Exception exception) {
            super(exception);
            this.owner = null;
        }

        public static Result success(String owner) {
            return new Result(owner);
        }

        public static Result failure(Exception exception) {
            return new Result(exception);
        }

        public boolean isCheckedOut() {
            return owner != null && !owner.isEmpty();
        }
    }
}
