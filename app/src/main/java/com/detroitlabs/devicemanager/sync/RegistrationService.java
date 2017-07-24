package com.detroitlabs.devicemanager.sync;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.detroitlabs.devicemanager.DmApplication;
import com.detroitlabs.devicemanager.constants.Constants;
import com.detroitlabs.devicemanager.db.Device;
import com.detroitlabs.devicemanager.db.DeviceDao;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;

import static com.detroitlabs.devicemanager.data.DatabaseContract.TABLE_DEVICES;

/**
 * 1. Register or update device information with server
 * 2. start listening to any remote changes on this device
 * 3. update cache if change is received
 */
public class RegistrationService extends IntentService {
    private static final String TAG = RegistrationService.class.getSimpleName();
    private static final String ACTION = TAG + ".ACTION";
    private static final int ACTION_REGISTER = 29;
    private static final int ACTION_SAVE_WITHOUT_REGISTER = 31;

    @Inject
    DeviceDao deviceDao;

    public RegistrationService() {
        super(TAG);
        DmApplication.getInjector().inject(this);
    }

    public static void initRegister(Context context) {
        Intent intent = new Intent(context, RegistrationService.class);
        intent.putExtra(ACTION, ACTION_REGISTER);
        context.startService(intent);
    }

    public static void saveWithoutRegister(Context context) {
        Intent intent = new Intent(context, RegistrationService.class);
        intent.putExtra(ACTION, ACTION_SAVE_WITHOUT_REGISTER);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Registration service started");
        switch (intent.getIntExtra(ACTION, 0)) {
            case ACTION_REGISTER:
                performRegistering();
                notifyActivity();
                Log.d(TAG, "Registration service completed");
                break;
            case ACTION_SAVE_WITHOUT_REGISTER:
                performSaveWithoutRegister();
                break;
        }
    }

    private void performSaveWithoutRegister() {
        Device device = DmApplication.getThisDevice();
        device.notRegisterable = true;
        deviceDao.insert(device);
    }

    private void performRegistering() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference rowRef = dbRef.child(TABLE_DEVICES).child(DmApplication.getThisDevice().serialNumber);
        rowRef.updateChildren(DmApplication.getThisDevice().toMap());
    }

    private void notifyActivity() {
        Intent localIntent = new Intent(Constants.BROADCAST_ACTION_REGISTER_RESULT);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
        Log.d(TAG, "Activity notified");
    }
}
