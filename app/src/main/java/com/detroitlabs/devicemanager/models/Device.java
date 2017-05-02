package com.detroitlabs.devicemanager.models;


import android.content.ContentValues;
import android.support.annotation.DrawableRes;

import com.detroitlabs.devicemanager.R;
import com.detroitlabs.devicemanager.constants.Platform;
import com.detroitlabs.devicemanager.data.DatabaseContract.DeviceColumns;
import com.detroitlabs.devicemanager.utils.StringUtil;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Device {
    public Platform platform;
    public String brandAndModel;
    public String version;
    public String screenSize;
    public String screenResolution;
    public String serialNumber;
    public String checkedOutBy;
    public boolean isCheckedOut;
    public long expireTime;

    public Device() {
        // Default constructor required for calls to DataSnapshot.getValue(Device.class)
    }

    public
    @DrawableRes
    int getIcon() {
        if (platform == Platform.ANDROID) {
            return R.drawable.ic_android_grey600_24dp;
        } else {
            return R.drawable.ic_apple_grey600_24dp;
        }
    }

    public boolean isCheckedOut() {
        return StringUtil.isNullOrEmpty(checkedOutBy);
    }

    @Exclude
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(DeviceColumns.BRAND_AND_MODEL, brandAndModel);
        values.put(DeviceColumns.PLATFORM, platform.toString());
        values.put(DeviceColumns.VERSION, version);
        values.put(DeviceColumns.SCREEN_RESOLUTION, screenResolution);
        values.put(DeviceColumns.SCREEN_SIZE, screenSize);
        return values;
    }
}
