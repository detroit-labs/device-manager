package com.detroitlabs.devicemanager.utils;


import static com.detroitlabs.devicemanager.constants.Constants.RESTRICTED_DOMAIN;
import static com.detroitlabs.devicemanager.constants.Constants.RESTRICTED_TEST_DEVICE_ACCOUNT;

public class AccountUtil {


    public static boolean isTestAccount(String email) {
        return RESTRICTED_TEST_DEVICE_ACCOUNT.equalsIgnoreCase(email);
    }

    public static boolean isLabsAccount(String email) {
        return email != null && email.contains(RESTRICTED_DOMAIN);
    }
}
