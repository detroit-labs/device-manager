package com.detroitlabs.devicemananger.models;


import com.detroitlabs.devicemananger.constants.FilterType;

import java.util.Map;
import java.util.Set;

public class Filter {
    public static class Options {
    public Map<FilterType, Set<String>> options;

    }

    public static class Selections {
    public Map<FilterType, Set<String>> selections;

    }
}
