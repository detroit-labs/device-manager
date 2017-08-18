package com.detroitlabs.devicemanager;

import android.arch.lifecycle.LifecycleActivity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.detroitlabs.devicemanager.databinding.ActivitySyncBinding;
import com.detroitlabs.devicemanager.di.DaggerActivityComponent;
import com.detroitlabs.devicemanager.sync.sequences.ManualSignInSyncSequence;
import com.detroitlabs.devicemanager.sync.sequences.InitialSyncSequence;
import com.detroitlabs.devicemanager.sync.Ui;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import javax.inject.Inject;

import dagger.Lazy;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class SyncActivity extends LifecycleActivity implements Ui, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = SyncActivity.class.getName();
    @Inject
    InitialSyncSequence syncSequence;

    @Inject
    Lazy<ManualSignInSyncSequence> manualSignInSyncSequenceLazy;

    private ActivitySyncBinding binding;

    private ActivityResult activityResult;

    private int requestCode;

    private SingleObserver<Boolean> autoSequenceObserver = new SingleObserver<Boolean>() {
        @Override
        public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

        }

        @Override
        public void onSuccess(@io.reactivex.annotations.NonNull Boolean isSyncSuccess) {
            Log.d(TAG, "initial sync sequence finished!!!");
            if (!isSyncSuccess) {
                displayUnauthorisedMsg();
            } else {
                startPagerActivity();
            }
        }

        @Override
        public void onError(@io.reactivex.annotations.NonNull Throwable e) {
            Log.e(TAG, "initial sync sequence error", e);
            displayUnauthorisedMsg();
        }
    };

    private Consumer<String> statusObserver = new Consumer<String>() {
        @Override
        public void accept(String status) throws Exception {
            binding.statusText.setText(status);
        }
    };

    private SingleObserver<Boolean> manualSequenceObserver = new SingleObserver<Boolean>() {
        @Override
        public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

        }

        @Override
        public void onSuccess(@io.reactivex.annotations.NonNull Boolean isSuccess) {
            if (isSuccess) {
                startPagerActivity();
            } else {
                displayUnauthorisedMsg();
            }
        }

        @Override
        public void onError(@io.reactivex.annotations.NonNull Throwable e) {

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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sync);
        binding.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manualSignInSyncSequenceLazy.get().run().subscribe(manualSequenceObserver);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == this.requestCode && activityResult != null) {
            activityResult.onActivityResult(resultCode, data);
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
        syncSequence.status().subscribe(statusObserver);
        syncSequence.run().subscribe(autoSequenceObserver);
    }

    private void startPagerActivity() {
        startActivity(new Intent(this, PagerActivity.class));
        finish();
    }

    private void displayUnauthorisedMsg() {
        binding.progressBar.setVisibility(View.GONE);
        binding.statusText.setText(R.string.not_authorised);
        binding.signInButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
