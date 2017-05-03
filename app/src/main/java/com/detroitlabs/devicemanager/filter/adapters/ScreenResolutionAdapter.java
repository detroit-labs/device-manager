package com.detroitlabs.devicemanager.filter.adapters;


import com.detroitlabs.devicemanager.R;
import com.detroitlabs.devicemanager.constants.FilterType;

public class ScreenResolutionAdapter extends FilterOptionAdapter {
    @Override
    public FilterType getFilterType() {
        return FilterType.SCREEN_RESOLUTION;
    }

    @Override
    public int getTitleRes() {
        return R.string.screen_resolution;
    }
}
