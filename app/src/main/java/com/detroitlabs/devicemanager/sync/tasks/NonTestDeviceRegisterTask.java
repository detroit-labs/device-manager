package com.detroitlabs.devicemanager.sync.tasks;


public class NonTestDeviceRegisterTask extends RegisterTask {
    @Override
    protected boolean isTestDevice() {
        return true;
    }
}
