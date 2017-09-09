package com.detroitlabs.devicemanager.sync.tasks;


import android.content.Context;
import android.util.Log;

import com.detroitlabs.devicemanager.DmApplication;
import com.detroitlabs.devicemanager.di.qualifiers.ApplicationContext;
import com.detroitlabs.devicemanager.sync.Result;
import com.detroitlabs.devicemanager.utils.DeviceUtil;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;

import io.reactivex.SingleEmitter;

import static com.detroitlabs.devicemanager.data.DatabaseContract.TABLE_DEVICES;

public class RegisterTask extends AsyncTask<Result> {

    private static final String TAG = RegisterTask.class.getName();
    private final Context context;

    @Inject
    public RegisterTask(@ApplicationContext Context context) {
        this.context = context;
    }

    @Override
    protected void task(final SingleEmitter<Result> emitter) {
        Log.d(TAG, "start register task");
        String serialNumber = DeviceUtil.getSerialNumber();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference rowRef = dbRef.child(TABLE_DEVICES).child(serialNumber);
        rowRef.setValue(DeviceUtil.readThisDevice(context), new DatabaseReference.CompletionListener() {
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
}
