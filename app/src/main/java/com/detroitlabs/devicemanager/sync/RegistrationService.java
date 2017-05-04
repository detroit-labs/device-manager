package com.detroitlabs.devicemanager.sync;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.detroitlabs.devicemanager.constants.Constants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.detroitlabs.devicemanager.data.DatabaseContract.TABLE_DEVICES;
import static com.detroitlabs.devicemanager.utils.DeviceUtil.THIS_DEVICE;


public class RegistrationService extends IntentService {
    public static final String TAG = RegistrationService.class.getSimpleName();

    public RegistrationService() {
        super(TAG);
    }

    public static void initRegister(Context context) {
        Intent intent = new Intent(context, RegistrationService.class);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "Registration service started");
        performRegistering();
        notifyActivity();
        Log.d(TAG, "Registration service completed");
    }

    private void performRegistering() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child(TABLE_DEVICES).child(THIS_DEVICE.serialNumber).setValue(THIS_DEVICE);
    }

    private void notifyActivity() {
        Intent localIntent = new Intent(Constants.BROADCAST_ACTION_REGISTER_RESULT);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
        Log.d(TAG, "Activity notified");
    }
}
