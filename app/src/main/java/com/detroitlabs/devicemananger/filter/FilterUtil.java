package com.detroitlabs.devicemananger.filter;


import com.detroitlabs.devicemananger.constants.FilterType;
import com.detroitlabs.devicemananger.models.Filter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FilterUtil {
    public static Filter.Selection filterSelection = new Filter.Selection();

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
