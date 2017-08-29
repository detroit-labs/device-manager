package com.detroitlabs.devicemanager.sync.tasks;


import android.util.Log;

import com.detroitlabs.devicemanager.DmApplication;
import com.detroitlabs.devicemanager.constants.Constants;
import com.detroitlabs.devicemanager.sync.Result;
import com.detroitlabs.devicemanager.utils.DeviceUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;

import io.reactivex.SingleEmitter;

import static com.detroitlabs.devicemanager.data.DatabaseContract.TABLE_DEVICES;

public class RegisterTask extends AsyncTask<Result> {

    private static final String TAG = RegisterTask.class.getName();

    @Inject
    public RegisterTask() {
    }

    @Override
    protected void task(final SingleEmitter<Result> emitter) {
        Log.d(TAG, "start register task");
        if (!isTestDevice()) {
            Log.e(TAG, "register failed: not testing device");
            emitter.onSuccess(Result.failure(new IllegalStateException("Cannot register on non test device")));
        } else if (DeviceUtil.isEmulator()) {
            Log.e(TAG, "register failed: running on emulator");
            emitter.onSuccess(Result.failure(new IllegalStateException("Cannot register on emulator")));
        } else {
            performRegister(emitter);
        }
    }

    private void performRegister(final SingleEmitter<Result> emitter) {
        String serialNumber = DmApplication.getSerialNumber();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference rowRef = dbRef.child(TABLE_DEVICES).child(serialNumber);
        rowRef.updateChildren(DmApplication.getThisDevice().toMap(), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Log.d(TAG, "register success");
                    emitter.onSuccess(Result.success());
                } else {
                    Log.e(TAG, "register failed", databaseError.toException());
                    emitter.onSuccess(Result.failure(databaseError.toException()));
                }
            }
        });
    }

    /**
     * By default only user who logged in with
     * test_user_1@detroitlabs.com is allow to register device
     */
    protected boolean isTestDevice() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null && user.getEmail() != null &&
                user.getEmail().equalsIgnoreCase(Constants.RESTRICTED_TEST_DEVICE_ACCOUNT);
    }
}
