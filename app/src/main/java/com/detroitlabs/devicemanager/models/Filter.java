package com.detroitlabs.devicemanager.models;


import com.detroitlabs.devicemanager.constants.FilterType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Filter {
    public static class Options {
        private Map<FilterType, Set<String>> options;

        public Options() {
            options = new HashMap<>();
        }

        public Set<String> getOptionValues(FilterType filterType) {
            Set<String> values = options.get(filterType);
            if (values != null) {
                return values;
            } else {
                return Collections.emptySet();
            }
        }

        public void addOptionValues(FilterType filterType, String value) {
            Set<String> values;
            if (options.containsKey(filterType)) {
                values = options.get(filterType);
            } else {
                values = new HashSet<>();
                options.put(filterType, values);
            }
            values.add(value);
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
            return new ArrayList<>(selection.get(FilterType.valueOf(key.toUpperCase())));
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
            if (selection.get(filterType).isEmpty()) {
                selection.remove(filterType);
            }
        }
    }
}
