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
import com.google.firebase.database.PropertyName;

import java.util.HashMap;
import java.util.Map;

import static com.detroitlabs.devicemanager.data.DatabaseContract.getString;

@IgnoreExtraProperties
public class Device {

    @PropertyName(DeviceColumns.PLATFORM)
    public Platform platform;

    @PropertyName(DeviceColumns.BRAND_AND_MODEL)
    public String brandAndModel;

    @PropertyName(DeviceColumns.VERSION)
    public String version;

    @PropertyName(DeviceColumns.SCREEN_SIZE)
    public String screenSize;

    @PropertyName(DeviceColumns.SCREEN_RESOLUTION)
    public String screenResolution;

    @PropertyName(DeviceColumns.SERIAL_NUMBER)
    public String serialNumber;

    @PropertyName(DeviceColumns.CHECKED_OUT_BY)
    public String checkedOutBy;

    @PropertyName(DeviceColumns.REQUESTED_BY)
    public String requestedBy;

    @PropertyName(DeviceColumns.YEAR_CLASS)
    public String yearClass;

    @PropertyName(DeviceColumns.IS_SAMSUNG)
    public String isSamsung;

    public Device() {
        // Default constructor required for calls to DataSnapshot.getValue(Device.class)
    }

    public Device(Cursor cursor) {
        version = getString(cursor, DeviceColumns.VERSION);
        yearClass = getString(cursor, DeviceColumns.YEAR_CLASS);
        isSamsung = getString(cursor, DeviceColumns.IS_SAMSUNG);
        screenSize = getString(cursor, DeviceColumns.SCREEN_SIZE);
        requestedBy = getString(cursor, DeviceColumns.REQUESTED_BY);
        serialNumber = getString(cursor, DeviceColumns.SERIAL_NUMBER);
        checkedOutBy = getString(cursor, DeviceColumns.CHECKED_OUT_BY);
        brandAndModel = getString(cursor, DeviceColumns.BRAND_AND_MODEL);
        brandAndModel = getString(cursor, DeviceColumns.BRAND_AND_MODEL);
        screenResolution = getString(cursor, DeviceColumns.SCREEN_RESOLUTION);
        platform = Platform.valueOf(getString(cursor, DeviceColumns.PLATFORM));
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
    public boolean hasRequest() {
        return !StringUtil.isNullOrEmpty(requestedBy);
    }

    @Exclude
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(DeviceColumns.VERSION, version);
        values.put(DeviceColumns.YEAR_CLASS, yearClass);
        values.put(DeviceColumns.IS_SAMSUNG, isSamsung);
        values.put(DeviceColumns.SCREEN_SIZE, screenSize);
        values.put(DeviceColumns.REQUESTED_BY, requestedBy);
        values.put(DeviceColumns.SERIAL_NUMBER, serialNumber);
        values.put(DeviceColumns.CHECKED_OUT_BY, checkedOutBy);
        values.put(DeviceColumns.PLATFORM, platform.toString());
        values.put(DeviceColumns.BRAND_AND_MODEL, brandAndModel);
        values.put(DeviceColumns.SCREEN_RESOLUTION, screenResolution);
        return values;
    }

    @Exclude
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(DeviceColumns.VERSION, version);
        map.put(DeviceColumns.YEAR_CLASS, yearClass);
        map.put(DeviceColumns.IS_SAMSUNG, isSamsung);
        map.put(DeviceColumns.SCREEN_SIZE, screenSize);
        map.put(DeviceColumns.SERIAL_NUMBER, serialNumber);
        map.put(DeviceColumns.PLATFORM, platform.toString());
        map.put(DeviceColumns.BRAND_AND_MODEL, brandAndModel);
        map.put(DeviceColumns.SCREEN_RESOLUTION, screenResolution);
        return map;
    }
}
