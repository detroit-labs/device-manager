package com.detroitlabs.devicemanager.db;

import android.arch.persistence.room.Entity;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import com.detroitlabs.devicemanager.R;
import com.detroitlabs.devicemanager.constants.FilterType;
import com.detroitlabs.devicemanager.constants.Platform;
import com.detroitlabs.devicemanager.data.DatabaseContract;
import com.detroitlabs.devicemanager.utils.DatetimeUtil;
import com.detroitlabs.devicemanager.utils.StringUtil;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import java.util.HashMap;
import java.util.Map;

import static com.detroitlabs.devicemanager.data.DatabaseContract.DeviceColumns.BRAND_AND_MODEL;
import static com.detroitlabs.devicemanager.data.DatabaseContract.DeviceColumns.CHECKED_OUT_BY;
import static com.detroitlabs.devicemanager.data.DatabaseContract.DeviceColumns.CHECK_OUT_TIME;
import static com.detroitlabs.devicemanager.data.DatabaseContract.DeviceColumns.IS_SAMSUNG;
import static com.detroitlabs.devicemanager.data.DatabaseContract.DeviceColumns.REQUESTED_BY;
import static com.detroitlabs.devicemanager.data.DatabaseContract.DeviceColumns.SCREEN_RESOLUTION;
import static com.detroitlabs.devicemanager.data.DatabaseContract.DeviceColumns.SCREEN_SIZE;
import static com.detroitlabs.devicemanager.data.DatabaseContract.DeviceColumns.SERIAL_NUMBER;
import static com.detroitlabs.devicemanager.data.DatabaseContract.DeviceColumns.YEAR_CLASS;

@Entity(primaryKeys = "serialNumber")
public class Device {

    /// hardware property
    @PropertyName(IS_SAMSUNG)
    public String isSamsung;
    @NonNull
    @PropertyName(SERIAL_NUMBER)
    public String serialNumber;
    public Platform platform;
    @PropertyName(BRAND_AND_MODEL)
    public String brandAndModel;
    public String version;
    @PropertyName(SCREEN_SIZE)
    public String screenSize;
    @PropertyName(SCREEN_RESOLUTION)
    public String screenResolution;
    @PropertyName(YEAR_CLASS)
    public String yearClass;

    /// soft property
    @PropertyName(REQUESTED_BY)
    public String requestedBy;
    @PropertyName(CHECKED_OUT_BY)
    public String checkedOutBy;
    @PropertyName(CHECK_OUT_TIME)
    public Long checkOutTime;

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
        map.put(DatabaseContract.DeviceColumns.PLATFORM, platform);
        map.put(DatabaseContract.DeviceColumns.YEAR_CLASS, yearClass);
        map.put(DatabaseContract.DeviceColumns.IS_SAMSUNG, isSamsung);
        map.put(DatabaseContract.DeviceColumns.SCREEN_SIZE, screenSize);
        map.put(DatabaseContract.DeviceColumns.SERIAL_NUMBER, serialNumber);
        map.put(DatabaseContract.DeviceColumns.CHECK_OUT_TIME, checkOutTime);
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

    @Exclude
    public String getCheckOutTime() {
        String formatDate = DatetimeUtil.formatDate(checkOutTime);
        return formatDate == null ? "" : formatDate;
    }

    @Override
    public String toString() {
        return "Device{" +
                "platform=" + platform +
                ", brandAndModel='" + brandAndModel + '\'' +
                ", version='" + version + '\'' +
                ", screenSize='" + screenSize + '\'' +
                ", screenResolution='" + screenResolution + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", checkedOutBy='" + checkedOutBy + '\'' +
                ", requestedBy='" + requestedBy + '\'' +
                ", yearClass='" + yearClass + '\'' +
                ", isSamsung='" + isSamsung + '\'' +
                ", checkOutTime=" + checkOutTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Device device = (Device) o;

        if (platform != device.platform) return false;
        if (brandAndModel != null ? !brandAndModel.equals(device.brandAndModel) : device.brandAndModel != null)
            return false;
        if (version != null ? !version.equals(device.version) : device.version != null)
            return false;
        if (screenSize != null ? !screenSize.equals(device.screenSize) : device.screenSize != null)
            return false;
        if (screenResolution != null ? !screenResolution.equals(device.screenResolution) : device.screenResolution != null)
            return false;
        if (!serialNumber.equals(device.serialNumber)) return false;
        if (checkedOutBy != null ? !checkedOutBy.equals(device.checkedOutBy) : device.checkedOutBy != null)
            return false;
        if (requestedBy != null ? !requestedBy.equals(device.requestedBy) : device.requestedBy != null)
            return false;
        if (yearClass != null ? !yearClass.equals(device.yearClass) : device.yearClass != null)
            return false;
        if (isSamsung != null ? !isSamsung.equals(device.isSamsung) : device.isSamsung != null)
            return false;
        return checkOutTime != null ? checkOutTime.equals(device.checkOutTime) : device.checkOutTime == null;

    }

    @Override
    public int hashCode() {
        int result = platform != null ? platform.hashCode() : 0;
        result = 31 * result + (brandAndModel != null ? brandAndModel.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (screenSize != null ? screenSize.hashCode() : 0);
        result = 31 * result + (screenResolution != null ? screenResolution.hashCode() : 0);
        result = 31 * result + serialNumber.hashCode();
        result = 31 * result + (checkedOutBy != null ? checkedOutBy.hashCode() : 0);
        result = 31 * result + (requestedBy != null ? requestedBy.hashCode() : 0);
        result = 31 * result + (yearClass != null ? yearClass.hashCode() : 0);
        result = 31 * result + (isSamsung != null ? isSamsung.hashCode() : 0);
        result = 31 * result + (checkOutTime != null ? checkOutTime.hashCode() : 0);
        return result;
    }
}
