package com.detroitlabs.devicemanager.data;


import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.detroitlabs.devicemanager.models.Device;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.detroitlabs.devicemanager.data.DatabaseContract.TABLE_DEVICES;

public class SyncingService extends IntentService {
    public static final String TAG = SyncingService.class.getSimpleName();

    public SyncingService() {
        super(TAG);
    }

    public static void initSync(Context context) {
        Intent intent = new Intent(context, SyncingService.class);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "Sync service started");
        performSyncing();
    }

    private void performSyncing() {
        FirebaseDatabase.getInstance().getReference().child(TABLE_DEVICES).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, dataSnapshot.getChildrenCount() + " rows of data need to be inserted");
                ContentValues[] contentValuesList = new ContentValues[((int) dataSnapshot.getChildrenCount())];
                int index = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Device value = snapshot.getValue(Device.class);
                    contentValuesList[index] = value.getContentValues();
                    index++;
                }
                getContentResolver().bulkInsert(DatabaseContract.DEVICE_URI, contentValuesList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
