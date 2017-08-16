package com.detroitlabs.devicemanager.di;


import android.app.Application;
import android.content.Context;

import com.detroitlabs.devicemanager.di.qualifiers.ApplicationContext;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    @Provides
    @ApplicationContext
    Context providesContext(Application application) {
        return application;
    }
}
