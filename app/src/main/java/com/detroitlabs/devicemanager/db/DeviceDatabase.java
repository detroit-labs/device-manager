package com.detroitlabs.devicemanager.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

@Database(entities = {Device.class}, version = 2)
@TypeConverters({PlatformConverter.class})
public abstract class DeviceDatabase extends RoomDatabase {
    public abstract DeviceDao deviceDao();
}