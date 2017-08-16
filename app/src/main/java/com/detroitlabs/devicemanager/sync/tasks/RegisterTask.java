package com.detroitlabs.devicemanager.sync.tasks;


import android.util.Log;

import com.detroitlabs.devicemanager.DmApplication;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;

import io.reactivex.SingleEmitter;

import static com.detroitlabs.devicemanager.data.DatabaseContract.TABLE_DEVICES;

public class RegisterTask extends AsyncTask<AsyncTask.Result> {

    private static final String TAG = RegisterTask.class.getName();

    @Inject
    public RegisterTask() {
    }

    @Override
    protected void task(final SingleEmitter<Result> emitter) {
        Log.d(TAG, "start register task");
        String serialNumber = DmApplication.getSerialNumber();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference rowRef = dbRef.child(TABLE_DEVICES).child(serialNumber);
        rowRef.updateChildren(DmApplication.getThisDevice().toMap(), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Log.d(TAG, "register success");
                    emitter.onSuccess(Result.empty());
                } else {
                    Log.e(TAG, "register failed", databaseError.toException());
                    emitter.onSuccess(new Result(databaseError.toException()));
                }
            }
        });
    }
}
