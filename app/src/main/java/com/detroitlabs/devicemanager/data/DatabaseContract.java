package com.detroitlabs.devicemanager.data;

import android.provider.BaseColumns;


public class DatabaseContract {
    public static final String TABLE_DEVICES = "devices";

    public static final class DeviceColumns implements BaseColumns {
        public static final String VERSION = "version";
        public static final String PLATFORM = "platform";
        public static final String YEAR_CLASS = "year_class";
        public static final String IS_SAMSUNG = "is_samsung";
        public static final String SCREEN_SIZE = "screen_size";
        public static final String REQUESTED_BY = "requested_by";
        public static final String SERIAL_NUMBER = "serial_number";
        public static final String CHECKED_OUT_BY = "checked_out_by";
        public static final String CHECK_OUT_TIME = "check_out_time";
        public static final String BRAND_AND_MODEL = "brand_and_model";
        public static final String SCREEN_RESOLUTION = "screen_resolution";
        public static final String LAST_KNOWN_BATTERY = "last_known_battery";
    }
}
