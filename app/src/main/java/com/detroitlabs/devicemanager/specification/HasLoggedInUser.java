package com.detroitlabs.devicemanager.specification;


import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HasLoggedInUser implements Specification {

    @Inject
    HasLoggedInUser() {
    }

    @Override
    public boolean isSatisfied() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }
}
