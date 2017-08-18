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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Device device = (Device) o;

        if (notRegisterable != device.notRegisterable) return false;
        if (platform != device.platform) return false;
        if (brandAndModel != null ? !brandAndModel.equals(device.brandAndModel) : device.brandAndModel != null)
            return false;
        if (version != null ? !version.equals(device.version) : device.version != null)
            return false;
        if (screenSize != null ? !screenSize.equals(device.screenSize) : device.screenSize != null)
            return false;
        if (screenResolution != null ? !screenResolution.equals(device.screenResolution) : device.screenResolution != null)
            return false;
        if (serialNumber != null ? !serialNumber.equals(device.serialNumber) : device.serialNumber != null)
            return false;
        if (checkedOutBy != null ? !checkedOutBy.equals(device.checkedOutBy) : device.checkedOutBy != null)
            return false;
        if (requestedBy != null ? !requestedBy.equals(device.requestedBy) : device.requestedBy != null)
            return false;
        if (yearClass != null ? !yearClass.equals(device.yearClass) : device.yearClass != null)
            return false;
        return isSamsung != null ? isSamsung.equals(device.isSamsung) : device.isSamsung == null;

    }

    @Override
    public int hashCode() {
        int result = platform != null ? platform.hashCode() : 0;
        result = 31 * result + (brandAndModel != null ? brandAndModel.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (screenSize != null ? screenSize.hashCode() : 0);
        result = 31 * result + (screenResolution != null ? screenResolution.hashCode() : 0);
        result = 31 * result + (serialNumber != null ? serialNumber.hashCode() : 0);
        result = 31 * result + (checkedOutBy != null ? checkedOutBy.hashCode() : 0);
        result = 31 * result + (requestedBy != null ? requestedBy.hashCode() : 0);
        result = 31 * result + (yearClass != null ? yearClass.hashCode() : 0);
        result = 31 * result + (isSamsung != null ? isSamsung.hashCode() : 0);
        result = 31 * result + (notRegisterable ? 1 : 0);
        return result;
    }
}
