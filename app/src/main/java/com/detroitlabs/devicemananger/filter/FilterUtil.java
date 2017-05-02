package com.detroitlabs.devicemananger.filter;


import com.detroitlabs.devicemananger.constants.FilterType;
import com.detroitlabs.devicemananger.filter.adapters.FilterOptionAdapter;
import com.detroitlabs.devicemananger.filter.adapters.PlatformFilterAdapter;
import com.detroitlabs.devicemananger.filter.adapters.ScreenResolutionAdapter;
import com.detroitlabs.devicemananger.filter.adapters.ScreenSizeAdapter;
import com.detroitlabs.devicemananger.filter.adapters.VersionFilterAdapter;
import com.detroitlabs.devicemananger.models.Filter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FilterUtil {
    public static Filter.Selection filterSelection = new Filter.Selection();

    static FilterOptionAdapter[] createAdapters() {
        return new FilterOptionAdapter[]{
                new PlatformFilterAdapter(),
                new VersionFilterAdapter(),
                new ScreenSizeAdapter(),
                new ScreenResolutionAdapter()
        };
    }

    public static void addSelection(FilterType filterType, String value) {
        filterSelection.addSelection(filterType, value);
    }

    public static void removeSelection(FilterType filterType, String value) {
        filterSelection.removeSelection(filterType, value);
    }


    public static Filter.Selection getFilterSelection() {
        return filterSelection;
    }

}
