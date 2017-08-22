package com.detroitlabs.devicemanager.sync.tasks;

import com.detroitlabs.devicemanager.data.DatabaseContract;
import com.detroitlabs.devicemanager.utils.DeviceUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;

import io.reactivex.SingleEmitter;

import static com.detroitlabs.devicemanager.data.DatabaseContract.TABLE_DEVICES;


public class GetOwnerTask extends AsyncTask<GetOwnerTask.Result> {
    @Inject
    public GetOwnerTask() {
    }

    @Override
    protected void task(final SingleEmitter<GetOwnerTask.Result> emitter) {
        FirebaseDatabase.getInstance().getReference()
                .child(TABLE_DEVICES)
                .child(DeviceUtil.getSerialNumber())
                .child(DatabaseContract.DeviceColumns.CHECKED_OUT_BY)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String checkedOutBy = dataSnapshot.getValue(String.class);
                        if (checkedOutBy == null) {
                            checkedOutBy = "";
                        }
                        emitter.onSuccess(new Result(checkedOutBy));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        emitter.onError(databaseError.toException());
                    }
                });
    }

    public static class Result extends AsyncTask.Result {
        public String owner;

        public Result(String owner) {
            this.owner = owner;
        }

        public boolean isSuccess() {
            return owner != null && !owner.isEmpty();
        }

        public static Result fail() {
            return new Result("");
        }
    }
}
