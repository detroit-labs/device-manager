package com.detroitlabs.devicemananger.filter.adapters;


import com.detroitlabs.devicemananger.constants.FilterType;

public class ScreenSizeAdapter extends FilterOptionAdapter {
    @Override
    public FilterType getFilterType() {
        return FilterType.SCREEN_SIZE;
    }
}
