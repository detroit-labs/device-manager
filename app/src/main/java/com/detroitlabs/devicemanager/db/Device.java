package com.detroitlabs.devicemanager.db;

import android.arch.persistence.room.Entity;
import android.support.annotation.DrawableRes;

import com.detroitlabs.devicemanager.R;
import com.detroitlabs.devicemanager.constants.Platform;
import com.detroitlabs.devicemanager.data.DatabaseContract;
import com.detroitlabs.devicemanager.utils.StringUtil;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import java.util.HashMap;
import java.util.Map;

@Entity(primaryKeys = "serialNumber")
public class Device {

    public Platform platform;
    @PropertyName("brand_and_model")
    public String brandAndModel;
    public String version;
    @PropertyName("screen_size")
    public String screenSize;
    @PropertyName("screen_resolution")
    public String screenResolution;
    @PropertyName("serial_number")
    public String serialNumber;
    @PropertyName("checked_out_by")
    public String checkedOutBy;
    @PropertyName("requested_by")
    public String requestedBy;
    @PropertyName("year_class")
    public String yearClass;
    @PropertyName("is_samsung")
    public String isSamsung;
    @Exclude
    public boolean notRegisterable;

    // TODO: 7/13/17 temporary wrapper method
    public static Device wrap(com.detroitlabs.devicemanager.models.Device device) {
        Device d = new Device();
        d.platform = device.platform;
        d.brandAndModel = device.brandAndModel;
        d.version = device.version;
        d.screenSize = device.screenSize;
        d.screenResolution = device.screenResolution;
        d.serialNumber = device.serialNumber;
        d.checkedOutBy = device.checkedOutBy;
        d.requestedBy = device.requestedBy;
        d.yearClass = device.yearClass;
        d.isSamsung = device.isSamsung;
        return d;
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
    @DrawableRes
    public int getIcon() {
        if (platform == Platform.ANDROID) {
            return R.drawable.ic_android_grey600_24dp;
        } else {
            return R.drawable.ic_apple_grey600_24dp;
        }
    }

    @Exclude
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(DatabaseContract.DeviceColumns.VERSION, version);
        map.put(DatabaseContract.DeviceColumns.YEAR_CLASS, yearClass);
        map.put(DatabaseContract.DeviceColumns.IS_SAMSUNG, isSamsung);
        map.put(DatabaseContract.DeviceColumns.SCREEN_SIZE, screenSize);
        map.put(DatabaseContract.DeviceColumns.SERIAL_NUMBER, serialNumber);
        map.put(DatabaseContract.DeviceColumns.PLATFORM, platform);
        map.put(DatabaseContract.DeviceColumns.BRAND_AND_MODEL, brandAndModel);
        map.put(DatabaseContract.DeviceColumns.SCREEN_RESOLUTION, screenResolution);
        return map;
    }
}
