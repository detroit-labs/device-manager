package com.detroitlabs.devicemanager.specification;


import android.os.Build;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class OreoOrHigher implements Specification {
    @Inject
    OreoOrHigher() {
    }

    @Override
    public boolean isSatisfied() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }
}
