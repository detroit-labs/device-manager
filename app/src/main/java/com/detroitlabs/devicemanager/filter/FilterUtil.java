package com.detroitlabs.devicemanager.filter;


import com.detroitlabs.devicemanager.constants.FilterType;
import com.detroitlabs.devicemanager.filter.adapters.FilterOptionAdapter;
import com.detroitlabs.devicemanager.filter.adapters.PlatformFilterAdapter;
import com.detroitlabs.devicemanager.filter.adapters.ScreenResolutionAdapter;
import com.detroitlabs.devicemanager.filter.adapters.ScreenSizeAdapter;
import com.detroitlabs.devicemanager.filter.adapters.VersionFilterAdapter;
import com.detroitlabs.devicemanager.models.Filter;

import java.util.List;
import java.util.Set;

public class FilterUtil {
    private static Filter.Selection filterSelection = new Filter.Selection();
    private static Filter.Options filterOptions = new Filter.Options();

    static FilterOptionAdapter[] createAdapters() {
        return new FilterOptionAdapter[]{
                new PlatformFilterAdapter(),
                new VersionFilterAdapter(),
                new ScreenSizeAdapter(),
                new ScreenResolutionAdapter()
        };
    }

    public static boolean firstTimeOpened() {
        return filterOptions.isEmpty();
    }

    public static void setAllOptions(Filter.Options options) {
        filterOptions = options;
    }

    public static Set<String> getAllOptionValues(FilterType filterType) {
        return filterOptions.getOptionValues(filterType);
    }

    public static void addSelection(FilterType filterType, String value) {
        filterSelection.addSelection(filterType, value);
    }

    public static void removeSelection(FilterType filterType, String value) {
        filterSelection.removeSelection(filterType, value);
    }

    public static boolean containsSelection(FilterType filterType, String value) {
        return filterSelection.containsSelection(filterType, value);
    }

    public static String convertFilterToQuerySelection() {
        if (!filterSelection.hasSelection()) {
            return null;
        } else {
            // filterType1 in ('value1', 'value2') and filterType2 in ('value1', 'value2')
            String selection = "";
            List<String> selectionTypes = filterSelection.getSelectionKeys();
            for (int i = 0; i < selectionTypes.size(); ++i) {
                if (i > 0) {
                    selection += " and ";
                }
                String selectionType = selectionTypes.get(i);
                selection += selectionType + " in (";
                selection += getArgs(filterSelection.getSelectionValues(selectionType));
                selection += ")";

            }
            return selection;
        }
    }

    private static String getArgs(List<String> values) {
        // 'value1', 'value2', 'value3'
        String args = "";
        for (int index = 0; index < values.size(); ++index) {
            if (index > 0) {
                args += ",";
            }
            args += "'";
            args += values.get(index);
            args += "'";
        }
        return args;
    }
}
