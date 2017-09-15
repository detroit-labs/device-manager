package com.detroitlabs.devicemanager.ui.filter.adapters;


import com.detroitlabs.devicemanager.R;
import com.detroitlabs.devicemanager.constants.FilterType;

public class VersionFilterAdapter extends FilterOptionAdapter {
    @Override
    public FilterType getFilterType() {
        return FilterType.VERSION;
    }

    @Override
    public int getTitleRes() {
        return R.string.version;
    }
}
