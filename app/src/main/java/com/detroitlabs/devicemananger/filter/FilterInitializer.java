package com.detroitlabs.devicemananger.filter;


import com.detroitlabs.devicemananger.filter.adapters.FilterOptionAdapter;
import com.detroitlabs.devicemananger.filter.adapters.PlatformFilterAdapter;
import com.detroitlabs.devicemananger.filter.adapters.ScreenResolutionAdapter;
import com.detroitlabs.devicemananger.filter.adapters.ScreenSizeAdapter;
import com.detroitlabs.devicemananger.filter.adapters.VersionFilterAdapter;

class FilterInitializer {

    static FilterOptionAdapter[] createAdapters() {
        return new FilterOptionAdapter[]{
                new PlatformFilterAdapter(),
                new VersionFilterAdapter(),
                new ScreenSizeAdapter(),
                new ScreenResolutionAdapter()
        };
    }
}
