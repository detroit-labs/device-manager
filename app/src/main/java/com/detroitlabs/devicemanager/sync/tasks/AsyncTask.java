package com.detroitlabs.devicemanager.sync.tasks;


import android.util.Log;

import com.detroitlabs.devicemanager.sync.Task;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

abstract class AsyncTask<T> implements Task {
    protected abstract void task(SingleEmitter<T> emitter);

    public Single<T> run() {
        return runOn(Schedulers.io());
    }

    public Single<T> runOn(Scheduler scheduler) {
        Single<T> single = Single.create(new SingleOnSubscribe<T>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<T> e) throws Exception {
                Log.d("AsyncTask", "Observable thread: " + Thread.currentThread().getName());
                task(e);
            }
        });
        return single.subscribeOn(scheduler).observeOn(AndroidSchedulers.mainThread());
    }
}
