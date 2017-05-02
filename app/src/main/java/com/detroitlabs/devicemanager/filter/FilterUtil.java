package com.detroitlabs.devicemanager.filter;


import com.detroitlabs.devicemanager.constants.FilterType;
import com.detroitlabs.devicemanager.filter.adapters.FilterOptionAdapter;
import com.detroitlabs.devicemanager.filter.adapters.PlatformFilterAdapter;
import com.detroitlabs.devicemanager.filter.adapters.ScreenResolutionAdapter;
import com.detroitlabs.devicemanager.filter.adapters.ScreenSizeAdapter;
import com.detroitlabs.devicemanager.filter.adapters.VersionFilterAdapter;
import com.detroitlabs.devicemanager.models.Filter;

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
