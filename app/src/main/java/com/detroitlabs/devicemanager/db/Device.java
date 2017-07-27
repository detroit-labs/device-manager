package com.detroitlabs.devicemanager.db;

import android.arch.persistence.room.Entity;
import android.support.annotation.DrawableRes;

import com.detroitlabs.devicemanager.R;
import com.detroitlabs.devicemanager.constants.FilterType;
import com.detroitlabs.devicemanager.constants.Platform;
import com.detroitlabs.devicemanager.data.DatabaseContract;
import com.detroitlabs.devicemanager.utils.StringUtil;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import java.util.HashMap;
import java.util.Map;

import static com.detroitlabs.devicemanager.data.DatabaseContract.DeviceColumns.*;

@Entity(primaryKeys = "serialNumber")
public class Device {

    public Platform platform;
    @PropertyName(BRAND_AND_MODEL)
    public String brandAndModel;
    public String version;
    @PropertyName(SCREEN_SIZE)
    public String screenSize;
    @PropertyName(SCREEN_RESOLUTION)
    public String screenResolution;
    @PropertyName(SERIAL_NUMBER)
    public String serialNumber;
    @PropertyName(CHECKED_OUT_BY)
    public String checkedOutBy;
    @PropertyName(REQUESTED_BY)
    public String requestedBy;
    @PropertyName(YEAR_CLASS)
    public String yearClass;
    @PropertyName(IS_SAMSUNG)
    public String isSamsung;
    @PropertyName(LAST_KNOWN_BATTERY)
    public String lastKnownBattery;
    @Exclude
    public boolean notRegisterable;

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

    @Exclude
    public String getFilterValue(FilterType filterType) {
        switch (filterType) {
            case PLATFORM:
                return platform.name();
            case VERSION:
                return version;
            case SCREEN_SIZE:
                return screenSize;
            case SCREEN_RESOLUTION:
                return screenResolution;
            case YEAR_CLASS:
                return yearClass;
            case IS_SAMSUNG:
                return isSamsung;
            default:
                return "LOL";
        }
    }
}
