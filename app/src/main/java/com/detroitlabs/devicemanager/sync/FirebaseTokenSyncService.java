package com.detroitlabs.devicemanager.sync;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.detroitlabs.devicemanager.utils.DeviceUtil;
import com.google.firebase.database.FirebaseDatabase;

import static com.detroitlabs.devicemanager.data.DatabaseContract.TABLE_DEVICES;


public class FirebaseTokenSyncService extends IntentService {
    private static final String TAG = FirebaseTokenSyncService.class.getSimpleName();
    private static final String EXTRA = TAG + ".EXTRA";
    private static final String EXTRA_TOKEN = EXTRA + ".TOKEN";
    private static final String EXTRA_SERIAL_NUMBER = EXTRA + ".SERIAL_NUMBER";

    public FirebaseTokenSyncService() {
        super(TAG);
    }

    public static void initSync(Context context, String token) {
        Intent intent = new Intent(context, FirebaseTokenSyncService.class);
        intent.putExtra(EXTRA_TOKEN, token);
        intent.putExtra(EXTRA_SERIAL_NUMBER, DeviceUtil.getSerialNumber());

        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        String token = intent.getStringExtra(EXTRA_TOKEN);
        String serialNumber = intent.getStringExtra(EXTRA_SERIAL_NUMBER);
        performSync(serialNumber, token);
    }

    private void performSync(String serialNumber, String token) {
        FirebaseDatabase.getInstance().getReference()
                .child(TABLE_DEVICES)
                .child(serialNumber)
                .child("token")
                .setValue(token);
    }
}
