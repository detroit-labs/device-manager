package com.detroitlabs.devicemanager.specification;


import javax.inject.Inject;

public class CanUpdateDevice implements Specification {

    private final IsRealDevice isRealDevice;
    private final IsTestAccount isTestAccount;
    private final HasGetSerialNumberPermission hasGetSerialNumberPermission;

    @Inject
    CanUpdateDevice(IsRealDevice isRealDevice,
                    IsTestAccount isTestAccount,
                    HasGetSerialNumberPermission hasGetSerialNumberPermission) {

        this.isRealDevice = isRealDevice;
        this.isTestAccount = isTestAccount;
        this.hasGetSerialNumberPermission = hasGetSerialNumberPermission;
    }
    
    @Override
    public boolean isSatisfied() {
        return isRealDevice.isSatisfied() &&
                isTestAccount.isSatisfied() &&
                hasGetSerialNumberPermission.isSatisfied();
    }
}
