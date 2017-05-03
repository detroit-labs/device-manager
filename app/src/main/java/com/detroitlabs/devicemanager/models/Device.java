package com.detroitlabs.devicemanager.models;


import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.DrawableRes;

import com.detroitlabs.devicemanager.R;
import com.detroitlabs.devicemanager.constants.Platform;
import com.detroitlabs.devicemanager.data.DatabaseContract.DeviceColumns;
import com.detroitlabs.devicemanager.utils.StringUtil;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import static com.detroitlabs.devicemanager.data.DatabaseContract.getString;

@IgnoreExtraProperties
public class Device {
    public Platform platform;
    public String brandAndModel;
    public String version;
    public String screenSize;
    public String screenResolution;
    public String serialNumber;
    public String checkedOutBy;

    public Device() {
        // Default constructor required for calls to DataSnapshot.getValue(Device.class)
    }

    public Device(Cursor cursor) {
        platform = Platform.valueOf(getString(cursor, DeviceColumns.PLATFORM));
        brandAndModel = getString(cursor, DeviceColumns.BRAND_AND_MODEL);
        version = getString(cursor, DeviceColumns.VERSION);
        screenSize = getString(cursor, DeviceColumns.SCREEN_SIZE);
        screenResolution = getString(cursor, DeviceColumns.SCREEN_RESOLUTION);
        serialNumber = getString(cursor, DeviceColumns.SERIAL_NUMBER);
        brandAndModel = getString(cursor, DeviceColumns.BRAND_AND_MODEL);
        checkedOutBy = getString(cursor, DeviceColumns.CHECKED_OUT_BY);
    }

    @Exclude
    @DrawableRes
    public int getIcon() {
        if (platform == Platform.ANDROID) {
            return R.drawable.ic_android_grey600_24dp;
        } else {
            return R.drawable.ic_apple_grey600_24dp;
        }
    }

    @Exclude
    public boolean isCheckedOut() {
        return !StringUtil.isNullOrEmpty(checkedOutBy);
    }

    @Exclude
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(DeviceColumns.BRAND_AND_MODEL, brandAndModel);
        values.put(DeviceColumns.PLATFORM, platform.toString());
        values.put(DeviceColumns.VERSION, version);
        values.put(DeviceColumns.SCREEN_RESOLUTION, screenResolution);
        values.put(DeviceColumns.SCREEN_SIZE, screenSize);
        values.put(DeviceColumns.SERIAL_NUMBER, serialNumber);
        values.put(DeviceColumns.CHECKED_OUT_BY, checkedOutBy);
        return values;
    }
}
