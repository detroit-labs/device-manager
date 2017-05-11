package com.detroitlabs.devicemanager.sync;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.detroitlabs.devicemanager.constants.Constants;
import com.detroitlabs.devicemanager.models.Device;
import com.detroitlabs.devicemanager.utils.DeviceUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.detroitlabs.devicemanager.data.DatabaseContract.TABLE_DEVICES;

/**
 * 1. Register or update device information with server
 * 2. start listening to any remote changes on this device
 * 3. update cache if change is received
 */
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
        DatabaseReference rowRef = dbRef.child(TABLE_DEVICES).child(DeviceUtil.getSerialNumber());
        rowRef.updateChildren(DeviceUtil.readThisDevice(getApplicationContext()).toMap());
    }

    private void notifyActivity() {
        Intent localIntent = new Intent(Constants.BROADCAST_ACTION_REGISTER_RESULT);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
        Log.d(TAG, "Activity notified");
    }
}
