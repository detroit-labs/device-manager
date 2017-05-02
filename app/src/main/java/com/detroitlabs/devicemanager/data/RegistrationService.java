package com.detroitlabs.devicemanager.data;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.detroitlabs.devicemanager.models.Device;
import com.detroitlabs.devicemanager.utils.DeviceUtil;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import static com.detroitlabs.devicemanager.data.DatabaseContract.TABLE_DEVICES;


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
        Log.d(TAG, "Registration service completed");
    }

    private void performRegistering() {
        Device device = DeviceUtil.getDeviceDetail(getApplicationContext());
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        Map<String, Device> devices = new HashMap<>();
        devices.put("DL-001", device);
        dbRef.child(TABLE_DEVICES).setValue(devices);
    }
}
