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
                        emitter.onSuccess(Result.success(checkedOutBy));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        emitter.onError(databaseError.toException());
                    }
                });
    }

    public static class Result extends com.detroitlabs.devicemanager.sync.Result {
        public final String owner;

        protected Result(String owner) {
            super(null);
            this.owner = owner;
        }

        protected Result(Exception exception) {
            super(exception);
            this.owner = null;
        }

        public static Result success(String owner) {
            return new Result(owner);
        }

        public static Result failure(Exception exception) {
            return new Result(exception);
        }

        public boolean isCheckedOut() {
            return owner != null && !owner.isEmpty();
        }
    }
}
