package com.detroitlabs.devicemanager;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.detroitlabs.devicemanager.databinding.ActivitySyncBinding;
import com.detroitlabs.devicemanager.sync.RegistrationService;
import com.detroitlabs.devicemanager.sync.SyncingService;
import com.detroitlabs.devicemanager.utils.DeviceUtil;
import com.detroitlabs.devicemanager.utils.PrefUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static com.detroitlabs.devicemanager.constants.Constants.BROADCAST_ACTION_REGISTER_RESULT;
import static com.detroitlabs.devicemanager.constants.Constants.BROADCAST_ACTION_SYNC_RESULT;

public class SyncActivity extends AppCompatActivity {

    private static final String TAG = SyncActivity.class.getName();
    private static final int PERMISSION_REQUEST_GET_ACCOUNTS = 7;
    private static final String TEST_USER_1_DETROITLABS = "test_user_1@detroitlabs.com";
    private ActivitySyncBinding binding;
    private RegistrationStatusReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sync);

        initReceiver();
        authenticate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_GET_ACCOUNTS) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                registerDevice();
                return;
            }
            syncDatabase();
        }
    }

    private void authenticate() {
        updateStatusText(R.string.authentication);
        FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    checkRegisterStateAndInitSyncing();
                } else {
                    Log.w(TAG, "linkWithCredential:failure", task.getException());
//                    startPagerActivity();
                    updateStatusText(R.string.not_authorized);
                    binding.progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initReceiver() {
        receiver = new RegistrationStatusReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_ACTION_REGISTER_RESULT);
        intentFilter.addAction(BROADCAST_ACTION_SYNC_RESULT);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentFilter);
    }

    private void checkRegisterStateAndInitSyncing() {
        if (DeviceUtil.hasGetAccountsPermission(this)) {
            registerDevice();
        } else {
            Log.d(TAG, "requesting permission to read EMAIL");
            DeviceUtil.requestGetAccountsPermission(this, PERMISSION_REQUEST_GET_ACCOUNTS);
        }
    }

    private void registerDevice() {
        if (usingTestingAccount(AccountManager.get(this).getAccountsByType("com.google"))) {
            updateStatusText(R.string.status_register_device);
            RegistrationService.initRegister(this);
            Log.d(TAG, "Registered device with service");
        } else {
            RegistrationService.saveWithoutRegister(this);
            Log.d(TAG, "Not registering device because it is not using the test account");
            syncDatabase();
        }
    }

    private boolean usingTestingAccount(Account[] accountsByType) {
        for (Account account : accountsByType) {
            if (account.name.equalsIgnoreCase(TEST_USER_1_DETROITLABS)) {
                return true;
            }
        }
        return false;
    }

    private void onRegisterSuccessful() {
        updateStatusText(R.string.status_register_complete);
        syncDatabase();
    }

    private void syncDatabase() {
        updateStatusText(R.string.status_sync_database);
        SyncingService.initSync(this);
    }

    private void onSyncSuccess() {
        updateStatusText(R.string.status_sync_database_complete);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startPagerActivity();
            }
        }, 1000);
    }

    private void startPagerActivity() {
        startActivity(new Intent(this, PagerActivity.class));
        finish();
    }

    private void updateStatusText(@StringRes int statusRes) {
        binding.statusText.setText(statusRes);
    }

    private class RegistrationStatusReceiver extends BroadcastReceiver {

        private RegistrationStatusReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BROADCAST_ACTION_SYNC_RESULT)) {
                Log.d(TAG, "Sync done");
                onSyncSuccess();
            } else if (action.equals(BROADCAST_ACTION_REGISTER_RESULT)) {
                Log.d(TAG, "Registration done");
                PrefUtil.setRegisterSuccessful(SyncActivity.this, true);
                onRegisterSuccessful();
            }
        }

    }
}
