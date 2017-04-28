package com.detroitlabs.devicemananger.models;


import android.support.annotation.DrawableRes;

import com.detroitlabs.devicemananger.R;
import com.detroitlabs.devicemananger.constants.Platform;
import com.detroitlabs.devicemananger.utils.StringUtil;

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

    public @DrawableRes int getIcon() {
        if (platform == Platform.ANDROID) {
            return R.drawable.ic_android_grey600_24dp;
        } else {
            return R.drawable.ic_apple_grey600_24dp;
        }
    }

    public boolean isCheckedOut() {
        return StringUtil.isNullOrEmpty(checkedOutBy);
    }
}
