package com.detroitlabs.devicemanager.sync.tasks;


import android.util.Log;

import com.detroitlabs.devicemanager.db.Device;
import com.detroitlabs.devicemanager.repository.DeviceRepository;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;

import io.reactivex.SingleEmitter;

import static com.detroitlabs.devicemanager.data.DatabaseContract.TABLE_DEVICES;

public class DbSyncTask extends AsyncTask<Boolean> {

    private static final String TAG = DbSyncTask.class.getName();
    private final DeviceRepository deviceRepo;
    private ChildEventListener childEventListener;

    @Inject
    public DbSyncTask(DeviceRepository deviceRepo) {
        this.deviceRepo = deviceRepo;
    }

    @Override
    protected void task(SingleEmitter<Boolean> emitter) {
        int rowsDeleted = deviceRepo.emptyDeviceTable();
        Log.d(TAG, rowsDeleted + " rows cleared in devices table");

        DatabaseReference tableDevice = FirebaseDatabase.getInstance().getReference()
                .child(TABLE_DEVICES);
        tableDevice.addChildEventListener(getChildEventListener());
        notifyWhenDone(tableDevice, emitter);

    }

    private void notifyWhenDone(DatabaseReference tableDevice, final SingleEmitter<Boolean> emitter) {

        tableDevice.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "Sync is done, rows inserted: " + dataSnapshot.getChildrenCount());
                emitter.onSuccess(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                emitter.onError(databaseError.toException());
            }
        });
    }

    private ChildEventListener getChildEventListener() {
        if (childEventListener == null) {
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Device device = dataSnapshot.getValue(Device.class);
                    deviceRepo.insert(device);
                    Log.d(TAG, device.serialNumber + " device inserted");
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Device device = dataSnapshot.getValue(Device.class);
                    deviceRepo.update(device);
                    Log.d(TAG, device.serialNumber + " device data updated");
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Device device = dataSnapshot.getValue(Device.class);
                    deviceRepo.delete(device);
                    Log.d(TAG, device.serialNumber + " device data removed from local db");

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
        }
        return childEventListener;
    }
}
