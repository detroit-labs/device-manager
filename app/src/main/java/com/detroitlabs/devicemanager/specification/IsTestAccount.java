package com.detroitlabs.devicemanager.specification;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import static com.detroitlabs.devicemanager.constants.Constants.RESTRICTED_TEST_DEVICE_ACCOUNT;

public class IsTestAccount implements Specification {
    @Inject
    IsTestAccount() {
    }

    @Override
    public boolean isSatisfied() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null && RESTRICTED_TEST_DEVICE_ACCOUNT.equalsIgnoreCase(user.getEmail());
    }
}
