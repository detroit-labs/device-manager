package com.detroitlabs.devicemanager.filter.adapters;


import com.detroitlabs.devicemanager.constants.FilterType;

public class PlatformFilterAdapter extends FilterOptionAdapter {

    @Override
    public FilterType getFilterType() {
        return FilterType.PLATFORM;
    }
}
