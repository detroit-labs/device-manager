package com.detroitlabs.devicemanager.sync.tasks;


import com.detroitlabs.devicemanager.constants.Constants;
import com.detroitlabs.devicemanager.utils.DeviceUtil;
import com.detroitlabs.devicemanager.utils.PrefUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import io.reactivex.SingleEmitter;

public class GetRegistrableTask extends AsyncTask<Boolean> {

    private final PrefUtil prefUtil;

    @Inject
    GetRegistrableTask(PrefUtil prefUtil) {
        this.prefUtil = prefUtil;
    }

    @Override
    protected void task(SingleEmitter<Boolean> emitter) {
        boolean isRegistrable = !DeviceUtil.isEmulator() && isTestAccount();
        prefUtil.saveIsRegistrable(isRegistrable);
        emitter.onSuccess(isRegistrable);
    }

    private boolean isTestAccount() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null && user.getEmail() != null &&
                user.getEmail().equalsIgnoreCase(Constants.RESTRICTED_TEST_DEVICE_ACCOUNT);
    }
}
