package com.detroitlabs.devicemanager.sync;

import io.reactivex.Single;


public interface Task<T> {
    Single<T> run();
}
