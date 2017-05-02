package com.detroitlabs.devicemananger.filter;


import com.detroitlabs.devicemananger.constants.FilterType;
import com.detroitlabs.devicemananger.models.Filter;

import java.util.HashSet;
import java.util.Set;

public class FilterUtil {
    public static Filter filter;

    public static void setQuery(FilterType filterType, Set<String> options) {
        filter.selections.put(filterType, options);
    }

    public static Set<String> getFilterOptions() {
        return null;
    }

    public static void updateFilter(FilterType filterType, String value, boolean isSelected) {
        Set<String> options;
        if (filter.selections.containsKey(filterType)) {
            options = filter.selections.get(filterType);
        } else {
            options = new HashSet<>();
        }
        if (isSelected) {
            options.add(value);
        } else {
            options.remove(value);
        }
        if (options.isEmpty()) {
            filter.selections.remove(filterType);
        } else {
            filter.selections.put(filterType, options);
        }
    }
}
