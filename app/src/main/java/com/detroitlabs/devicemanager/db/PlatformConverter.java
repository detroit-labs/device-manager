package com.detroitlabs.devicemanager.db;

import android.arch.persistence.room.TypeConverter;

import com.detroitlabs.devicemanager.constants.Platform;

public class PlatformConverter {
    @TypeConverter
    public static Platform fromString(String platform) {
        return Platform.valueOf(platform);
    }

    @TypeConverter
    public static String enumToString(Platform platform) {
        return platform.name();
    }
}
