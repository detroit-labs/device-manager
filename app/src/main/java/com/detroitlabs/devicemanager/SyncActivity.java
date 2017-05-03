package com.detroitlabs.devicemanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.detroitlabs.devicemanager.data.RegistrationService;
import com.detroitlabs.devicemanager.data.SyncingService;
import com.detroitlabs.devicemanager.databinding.ActivitySyncBinding;
import com.detroitlabs.devicemanager.utils.DeviceUtil;
import com.detroitlabs.devicemanager.utils.PrefUtil;

import static com.detroitlabs.devicemanager.constants.Constants.BROADCAST_ACTION_REGISTER_RESULT;
import static com.detroitlabs.devicemanager.constants.Constants.BROADCAST_ACTION_SYNC_RESULT;
import static com.detroitlabs.devicemanager.constants.Constants.PREF_REGISTER_SUCCEEDED;

public class SyncActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_GET_ACCOUNTS = 7;
    private static final String TAG = SyncActivity.class.getSimpleName();
    private int progress = 0;
    private ActivitySyncBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Start Syncing Activity");
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sync);

        initReceiver();
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

    private void initReceiver() {
        RegistrationStatusReceiver receiver = new RegistrationStatusReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_ACTION_REGISTER_RESULT);
        intentFilter.addAction(BROADCAST_ACTION_SYNC_RESULT);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentFilter);
    }

    private void checkRegisterState() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean registerSucceeded = preferences.getBoolean(PREF_REGISTER_SUCCEEDED, false);
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
        updateProgressBar(R.string.status_register_device);
        RegistrationService.initRegister(this);
    }

    private void syncDatabase() {
        updateProgressBar(R.string.status_sync_database);
        SyncingService.initSync(this);
    }

    private void onSyncSuccess() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void onRegisterSuccessful() {
        // proceed to
    }

    private void updateProgressBar(@StringRes int statusTextRes) {
        progress += 20;
        binding.progressBar.setProgress(progress);
        binding.statusText.setText(statusTextRes);
    }

    private class RegistrationStatusReceiver extends BroadcastReceiver {

        private RegistrationStatusReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Registration done");
            String action = intent.getAction();
            if (action.equals(BROADCAST_ACTION_SYNC_RESULT)) {
                onSyncSuccess();
            } else if (action.equals(BROADCAST_ACTION_REGISTER_RESULT)) {
                PrefUtil.setRegisterSuccessful(SyncActivity.this, true);
                onRegisterSuccessful();

            }
        }

    }
}
