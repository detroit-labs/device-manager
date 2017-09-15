package com.detroitlabs.devicemanager.sync.sequences;


import com.detroitlabs.devicemanager.sync.Task;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

abstract class AsyncTaskSequence<T> implements Task<T> {
    protected final PublishSubject<String> statusSubject = PublishSubject.create();

    public Observable<String> status() {
        return statusSubject;
    }

    protected void updateStatus(String status) {
        statusSubject.onNext(status);
    }
}
