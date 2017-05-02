package com.detroitlabs.devicemananger.models;


import com.detroitlabs.devicemananger.constants.FilterType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Filter {
    public static class Options {
        private Map<FilterType, List<String>> options;

        public List<String> getOptionValues(FilterType filterType) {
            return new ArrayList<>(options.get(filterType));
        }
    }

    public static class Selection {
        private Map<FilterType, Set<String>> selection;

        public Selection() {
            selection = new HashMap<>();
        }
        public boolean hasSelection() {
            return selection != null && !selection.isEmpty();
        }

        public List<String> getSelectionKeys() {
            List<String> keys = new ArrayList<>(selection.keySet().size());
            for (FilterType filterType : selection.keySet()) {
                keys.add(filterType.toString());
            }
            return keys;
        }

        public List<String> getSelectionValues(String key) {
            return new ArrayList<>(selection.get(FilterType.valueOf(key)));
        }

        public void addSelection(FilterType filterType, Set<String> values) {
            selection.put(filterType, values);
        }

        public void addSelection(FilterType filterType, String value) {
            Set<String> values;
            if (selection.containsKey(filterType)) {
                values = selection.get(filterType);
            } else {
                values = new HashSet<>();
                selection.put(filterType, values);
            }
            values.add(value);
        }

        public void removeSelection(FilterType filterType, String value) {
            for (String v : selection.get(filterType)) {
                if (v.equalsIgnoreCase(value)) {
                    selection.get(filterType).remove(v);
                    break;
                }
            }
        }
    }
}
