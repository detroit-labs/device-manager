package com.detroitlabs.devicemanager;

import android.app.Activity;
import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.detroitlabs.devicemanager.di.AppComponent;
import com.detroitlabs.devicemanager.di.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import io.fabric.sdk.android.Fabric;


public class DmApplication extends Application {
    private AppComponent injector;
    private static DmApplication instance;

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        instance = this;
        injector = DaggerAppComponent.builder().application(this).build();
    }

    public static AppComponent getInjector() {
        return instance.injector;
    }
}
