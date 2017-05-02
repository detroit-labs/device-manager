package com.detroitlabs.devicemanager;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.detroitlabs.devicemanager.data.RegistrationService;
import com.detroitlabs.devicemanager.data.SyncingService;
import com.detroitlabs.devicemanager.utils.DeviceUtil;

import static com.detroitlabs.devicemanager.constants.Contants.PREF_KEY_REGISTER_SUCCEEDED;

public class SyncActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_GET_ACCOUNTS = 7;
    private static final String TAG = SyncActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Start Syncing Activity");
        super.onCreate(savedInstanceState);

        checkRegisterState();
        syncDatabase();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_GET_ACCOUNTS:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "permission is granted");
                    registerDevice();
                }
        }
    }

    private void checkRegisterState() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean registerSucceeded = preferences.getBoolean(PREF_KEY_REGISTER_SUCCEEDED, false);
        Log.d(TAG, "this device has " + (registerSucceeded ? "" : "not ") + "been registered");
        if (!registerSucceeded) {
            if (DeviceUtil.hasGetAccountsPermission(this)) {
                registerDevice();
            } else {
                Log.d(TAG, "requesting permission to read EMAIL");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.GET_ACCOUNTS}, PERMISSION_REQUEST_GET_ACCOUNTS);
            }
        }
    }

    private void registerDevice() {
        RegistrationService.initRegister(this);
    }

    private void syncDatabase() {
        SyncingService.initSync(this);
    }
}
