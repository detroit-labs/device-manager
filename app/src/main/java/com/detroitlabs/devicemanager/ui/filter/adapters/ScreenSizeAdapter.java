package com.detroitlabs.devicemanager.ui.filter.adapters;


import com.detroitlabs.devicemanager.R;
import com.detroitlabs.devicemanager.constants.FilterType;

public class ScreenSizeAdapter extends FilterOptionAdapter {
    @Override
    public FilterType getFilterType() {
        return FilterType.SCREEN_SIZE;
    }

    @Override
    public int getTitleRes() {
        return R.string.screen_size;
    }
}
