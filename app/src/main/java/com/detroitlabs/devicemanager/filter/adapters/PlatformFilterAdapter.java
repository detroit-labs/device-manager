package com.detroitlabs.devicemanager.filter.adapters;


import android.support.annotation.StringRes;

import com.detroitlabs.devicemanager.R;
import com.detroitlabs.devicemanager.constants.FilterType;

public class PlatformFilterAdapter extends FilterOptionAdapter {

    @Override
    public FilterType getFilterType() {
        return FilterType.PLATFORM;
    }

    @Override
    public @StringRes int getTitleRes() {
        return R.string.platform;
    }
}
