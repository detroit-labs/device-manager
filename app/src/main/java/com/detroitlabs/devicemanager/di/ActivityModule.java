package com.detroitlabs.devicemanager.di;


import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.detroitlabs.devicemanager.di.qualifiers.ActivityContext;
import com.detroitlabs.devicemanager.di.scopes.PerActivity;

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
}
