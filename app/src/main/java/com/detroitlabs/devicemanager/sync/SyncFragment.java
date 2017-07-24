package com.detroitlabs.devicemanager.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.detroitlabs.devicemanager.R;
import com.detroitlabs.devicemanager.databinding.FragSyncBinding;
import com.detroitlabs.devicemanager.utils.DeviceUtil;
import com.detroitlabs.devicemanager.utils.PrefUtil;
import com.detroitlabs.devicemanager.utils.ProgressBarUtil;

import static com.detroitlabs.devicemanager.constants.Constants.BROADCAST_ACTION_REGISTER_RESULT;
import static com.detroitlabs.devicemanager.constants.Constants.BROADCAST_ACTION_SYNC_RESULT;


public class SyncFragment extends Fragment {
    public interface OnSyncFinishListener {
        void onSyncFinish();
    }

    private static final String TEST_USER_1_DETROITLABS = "test_user_1@detroitlabs.com";
    private static final String TAG = SyncFragment.class.getSimpleName();
    private static final int PERMISSION_REQUEST_GET_ACCOUNTS = 7;

    private FragSyncBinding binding;
    private RegistrationStatusReceiver receiver;
    private OnSyncFinishListener onSyncFinishListener;
    private int progress;

    public static SyncFragment newInstance() {
        Bundle args = new Bundle();
        SyncFragment fragment = new SyncFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragSyncBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        initReceiver();
        checkRegisterStateAndInitSyncing();
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    public void setOnSyncFinishListener(OnSyncFinishListener listener) {
        this.onSyncFinishListener = listener;
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

    private boolean usingTestingAccount(Account[] accountsByType) {
        for (Account account : accountsByType) {
            if (account.name.equalsIgnoreCase(TEST_USER_1_DETROITLABS)) {
                return true;
            }
        }
        return false;
    }

    private void initReceiver() {
        receiver = new RegistrationStatusReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_ACTION_REGISTER_RESULT);
        intentFilter.addAction(BROADCAST_ACTION_SYNC_RESULT);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, intentFilter);
    }

    private void checkRegisterStateAndInitSyncing() {
        if (DeviceUtil.hasGetAccountsPermission(getContext())) {
            registerDevice();
        } else {
            Log.d(TAG, "requesting permission to read EMAIL");
            DeviceUtil.requestGetAccountsPermission(this, PERMISSION_REQUEST_GET_ACCOUNTS);
        }
    }

    private void registerDevice() {
        if (usingTestingAccount(AccountManager.get(getContext()).getAccountsByType("com.google"))) {
            updateProgressBar(R.string.status_register_device, 20);
            RegistrationService.initRegister(getContext());
            Log.d(TAG, "Registered device with service");
        } else {
            RegistrationService.saveWithoutRegister(getContext());
            Log.d(TAG, "Not registering device because it is not using the test account");
            syncDatabase();
        }
    }

    private void onRegisterSuccessful() {
        updateProgressBar(R.string.status_register_complete, 60);
        syncDatabase();
    }

    private void syncDatabase() {
        updateProgressBar(R.string.status_sync_database, 80);
        SyncingService.initSync(getContext());
    }

    private void onSyncSuccess() {
        updateProgressBar(R.string.status_sync_database_complete, 100);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onSyncFinishListener.onSyncFinish();
            }
        }, 1500);
    }

    private void updateProgressBar(@StringRes int statusTextRes, int progress) {
        this.progress += progress;
        ProgressBarUtil.animateToPercent(binding.progressBar, this.progress);
        binding.statusText.setText(statusTextRes);
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
                PrefUtil.setRegisterSuccessful(getContext(), true);
                onRegisterSuccessful();
            }
        }

    }
}
