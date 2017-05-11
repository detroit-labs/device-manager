package com.detroitlabs.devicemanager.filter.adapters;


import com.detroitlabs.devicemanager.R;
import com.detroitlabs.devicemanager.constants.FilterType;

public class IsSamsungAdapter extends FilterOptionAdapter {
    @Override
    public FilterType getFilterType() {
        return FilterType.IS_SAMSUNG;
    }

    @Override
    public int getTitleRes() {
        return R.string.is_samsung;
    }
}
