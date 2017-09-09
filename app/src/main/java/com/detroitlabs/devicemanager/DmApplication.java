package com.detroitlabs.devicemanager;

import android.app.Activity;
import android.app.Application;

import com.detroitlabs.devicemanager.db.Device;
import com.detroitlabs.devicemanager.di.AppComponent;
import com.detroitlabs.devicemanager.di.DaggerAppComponent;
import com.detroitlabs.devicemanager.utils.DeviceUtil;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;


public class DmApplication extends Application {
    private static Device device;
    private AppComponent injector;
    private static DmApplication instance;

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        device = DeviceUtil.readThisDevice(this);
        instance = this;
        injector = DaggerAppComponent.builder().application(this).build();
    }

    public static Device getThisDevice() {
        return device;
    }

    public static AppComponent getInjector() {
        return instance.injector;
    }
}
