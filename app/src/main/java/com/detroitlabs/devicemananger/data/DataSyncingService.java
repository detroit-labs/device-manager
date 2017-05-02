package com.detroitlabs.devicemananger.data;


import com.detroitlabs.devicemananger.models.Device;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DataSyncingService {
    // get list of devices
    public void sync() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("devices");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Device device = data.getValue(Device.class);
                    // store in db
                    // upon db store success, update ui
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void insertDevice() {

    }
}
