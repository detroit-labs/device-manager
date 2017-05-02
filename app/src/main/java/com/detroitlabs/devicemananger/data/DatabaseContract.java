package com.detroitlabs.devicemananger.data;

import android.net.Uri;
import android.provider.BaseColumns;


public class DatabaseContract {
    public static final String TABLE_DEVICES = "devices";

    public static final String PATH_FILTERS = "filters";

    public static final class DeviceColumns implements BaseColumns {
        public static final String PLATFORM = "platform";
        public static final String BRAND_AND_MODEL = "brand_and_model";
        public static final String VERSION = "version";
        public static final String SCREEN_SIZE = "screen_size";
        public static final String SCREEN_RESOLUTION = "screen_resolution";
        public static final String SERIAL_NUMBER = "serial_number";
        public static final String CHECKED_OUT_BY = "checked_out_by";
    }

    public static final String CONTENT_AUTHORITY = "com.detroitlabs.devicemanager";

    public static final Uri FILTER_URI = new Uri.Builder().scheme("content")
            .authority(CONTENT_AUTHORITY)
            .appendPath(TABLE_DEVICES)
            .appendPath(PATH_FILTERS)
            .build();
}
