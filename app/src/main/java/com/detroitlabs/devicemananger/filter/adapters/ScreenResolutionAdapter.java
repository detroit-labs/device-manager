package com.detroitlabs.devicemananger.filter.adapters;


import com.detroitlabs.devicemananger.constants.FilterType;

public class ScreenResolutionAdapter extends FilterOptionAdapter {
    @Override
    public FilterType getFilterType() {
        return FilterType.SCREEN_RESOLUTION;
    }
}
