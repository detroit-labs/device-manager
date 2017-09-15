package com.detroitlabs.devicemanager.notification;

import com.detroitlabs.devicemanager.sync.FirebaseTokenSyncService;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        // TODO: 8/14/17 need to sign in
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        FirebaseTokenSyncService.initSync(getApplicationContext(), refreshedToken);
    }
}
