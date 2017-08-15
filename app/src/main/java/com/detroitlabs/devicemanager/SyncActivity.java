package com.detroitlabs.devicemanager;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.detroitlabs.devicemanager.databinding.ActivitySyncBinding;
import com.detroitlabs.devicemanager.di.DaggerActivityComponent;
import com.detroitlabs.devicemanager.sync.DeviceUpdateService;
import com.detroitlabs.devicemanager.sync.InitialSyncSequence;
import com.detroitlabs.devicemanager.sync.RegistrationService;
import com.detroitlabs.devicemanager.sync.SyncingService;
import com.detroitlabs.devicemanager.sync.Ui;
import com.detroitlabs.devicemanager.utils.DeviceUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

import static com.detroitlabs.devicemanager.data.DatabaseContract.TABLE_DEVICES;

public class SyncActivity extends AppCompatActivity implements Ui, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = SyncActivity.class.getName();
    private static final int PERMISSION_REQUEST_GET_ACCOUNTS = 7;
    private static final String TEST_USER_1_DETROITLABS = "test_user_1@detroitlabs.com";
    @Inject
    InitialSyncSequence syncSequence;

    private ActivitySyncBinding binding;

    private ActivityResult activityResult;

    private int requestCode;

    private SingleObserver<? super Boolean> syncSequenceObserver = new SingleObserver<Boolean>() {
        @Override
        public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

        }

        @Override
        public void onSuccess(@io.reactivex.annotations.NonNull Boolean aBoolean) {
            Log.d(TAG, "initial sync sequence finished!!!");
        }

        @Override
        public void onError(@io.reactivex.annotations.NonNull Throwable e) {
            Log.e(TAG, "initial sync sequence error", e);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DaggerActivityComponent.builder()
                .appComponent(DmApplication.getInjector())
                .fragmentActivity(this)
                .ui(this)
                .listener(this)
                .build()
                .inject(this);
        super.onCreate(savedInstanceState);
        // TODO: 8/11/17 inject here
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sync);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == this.requestCode && activityResult != null) {
            activityResult.onActivityResult(resultCode, data);
        }
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

    @Override
    public void startActivityForResult(Intent intent, int requestCode, ActivityResult activityResult) {
        this.requestCode = requestCode;
        this.activityResult = activityResult;
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onStart() {
        super.onStart();
        syncSequence.run().subscribe(syncSequenceObserver);
    }

    private void authenticate() {
        updateStatusText(R.string.authentication);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (!isSignedIn()) {
            auth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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
        } else {
            checkRegisterStateAndInitSyncing();
        }
    }

    private boolean isSignedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
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

            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference rowRef = dbRef.child(TABLE_DEVICES).child(DmApplication.getThisDevice().serialNumber);
            rowRef.updateChildren(DmApplication.getThisDevice().toMap());

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
        DeviceUpdateService.updateLastKnownBattery(this, DeviceUtil.getBatteryLevel(this));
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
