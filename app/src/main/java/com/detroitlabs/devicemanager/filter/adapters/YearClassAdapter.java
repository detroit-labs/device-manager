package com.detroitlabs.devicemanager.filter.adapters;

import com.detroitlabs.devicemanager.R;
import com.detroitlabs.devicemanager.constants.FilterType;


public class YearClassAdapter extends FilterOptionAdapter {
    @Override
    public FilterType getFilterType() {
        return FilterType.YEAR_CLASS;
    }

    @Override
    public int getTitleRes() {
        return R.string.year_class;
    }
}
