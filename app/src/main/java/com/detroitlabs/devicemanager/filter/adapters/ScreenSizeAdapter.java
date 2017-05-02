package com.detroitlabs.devicemanager.filter.adapters;


import com.detroitlabs.devicemanager.constants.FilterType;

public class ScreenSizeAdapter extends FilterOptionAdapter {
    @Override
    public FilterType getFilterType() {
        return FilterType.SCREEN_SIZE;
    }
}
