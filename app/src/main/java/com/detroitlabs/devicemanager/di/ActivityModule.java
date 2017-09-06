package com.detroitlabs.devicemanager.di;


import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.detroitlabs.devicemanager.di.qualifiers.ActivityContext;
import com.detroitlabs.devicemanager.di.scopes.PerActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {
    @PerActivity
    @Provides
    @ActivityContext
    Context providesActivityContext(FragmentActivity fragmentActivity) {
        return fragmentActivity;
    }

    @Provides
    RxPermissions providesRxPermissions(@ActivityContext Context context) {
        return new RxPermissions((Activity) context);
    }
}
