package com.detroitlabs.devicemanager.di;


import android.app.Application;
import android.arch.persistence.room.Room;

import com.detroitlabs.devicemanager.db.DeviceDao;
import com.detroitlabs.devicemanager.db.DeviceDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    @Provides
    @Singleton
    DeviceDatabase providesDeviceDagabase(Application application) {
        return Room.databaseBuilder(application, DeviceDatabase.class, "device.db").build();
    }

    @Provides
    @Singleton
    DeviceDao provicesDeviceDao(DeviceDatabase db) {
        return db.deviceDao();
    }
}
