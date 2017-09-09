package com.detroitlabs.devicemanager.specification;


import com.detroitlabs.devicemanager.utils.DeviceUtil;

import javax.inject.Inject;

public class IsRealDevice implements Specification {

    @Inject
    public IsRealDevice() {
    }

    @Override
    public boolean isSatisfied() {
        return !DeviceUtil.isEmulator();
    }
}
