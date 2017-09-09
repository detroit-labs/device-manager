package com.detroitlabs.devicemanager.di;


import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;

import com.detroitlabs.devicemanager.di.qualifiers.ApplicationContext;
import com.detroitlabs.devicemanager.notification.DefaultNotificationBuilder;
import com.detroitlabs.devicemanager.notification.NotificationBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    @Provides
    @ApplicationContext
    @Singleton
    Context providesContext(Application application) {
        return application;
    }

    @Provides
    @Singleton
    NotificationManager providesNotificationManager(@ApplicationContext Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Provides
    @Singleton
    NotificationBuilder providesBuilder() {
        return new DefaultNotificationBuilder();
    }

}
