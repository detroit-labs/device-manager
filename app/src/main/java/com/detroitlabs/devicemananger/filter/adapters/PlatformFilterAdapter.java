package com.detroitlabs.devicemananger.filter.adapters;


import com.detroitlabs.devicemananger.constants.FilterType;

public class PlatformFilterAdapter extends FilterOptionAdapter {

    @Override
    public FilterType getFilterType() {
        return FilterType.PLATFORM;
    }
}
