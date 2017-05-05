package com.detroitlabs.devicemanager.sync;


import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.detroitlabs.devicemanager.constants.Constants;
import com.detroitlabs.devicemanager.data.DatabaseContract;
import com.detroitlabs.devicemanager.models.Device;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.detroitlabs.devicemanager.data.DatabaseContract.DEVICE_URI;
import static com.detroitlabs.devicemanager.data.DatabaseContract.TABLE_DEVICES;

public class SyncingService extends Service {
    public static final String TAG = SyncingService.class.getSimpleName();
    private static final int ACTION_REGISTER = 11;
    private static final int ACTION_UNREGISTER = 13;
    private static final int ACTION_SINGLE_SYNC = 17;
    private static final String ACTION = TAG + ".ACTION";

    private ChildEventListener childEventListener;
    private ServiceHandler serviceHandler;

    public static void initSync(Context context) {
        Intent intent = new Intent(context, SyncingService.class);
        intent.putExtra(ACTION, ACTION_SINGLE_SYNC);
        context.startService(intent);
    }

    public static void registerSync(Context context) {
        Intent intent = new Intent(context, SyncingService.class);
        intent.putExtra(ACTION, ACTION_REGISTER);
        context.startService(intent);
    }

    public static void unregisterSync(Context context) {
        Intent intent = new Intent(context, SyncingService.class);
        intent.putExtra(ACTION, ACTION_UNREGISTER);
        context.startService(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread("SyncingServiceAction");
        thread.start();
        serviceHandler = new ServiceHandler(thread.getLooper());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service hashcode: " + this.hashCode());
        int action = intent.getIntExtra(ACTION, 0);
        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = action;
        serviceHandler.sendMessage(msg);
        return super.onStartCommand(intent, flags, startId);
    }

    private void performUnregisterSyncing() {
        FirebaseDatabase.getInstance().getReference()
                .child(TABLE_DEVICES)
                .removeEventListener(getChildEventListener());
    }

    private void performRegisterSyncing() {
        FirebaseDatabase.getInstance().getReference()
                .child(TABLE_DEVICES)
                .addChildEventListener(getChildEventListener());
    }

    private ChildEventListener getChildEventListener() {
        if (childEventListener == null) {
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Log.d(TAG, "device data change detected");
                    Device device = dataSnapshot.getValue(Device.class);
                    ContentValues values = device.getContentValues();
                    getContentResolver().update(DEVICE_URI, values, null, null);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

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

    private void notifySingleSyncComplete() {
        Intent localIntent = new Intent(Constants.BROADCAST_ACTION_SINGLE_SYNC_RESULT);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
        Log.d(TAG, "Activity notified");
    }

    private final class ServiceHandler extends Handler {
        ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case ACTION_REGISTER:
                    performRegisterSyncing();
                    break;
                case ACTION_UNREGISTER:
                    performUnregisterSyncing();
                    stopSelf();
                    break;
                case ACTION_SINGLE_SYNC:
                    performSyncing();
                    notifySingleSyncComplete();
                    break;
                default:
                    throw new IllegalStateException("Illegal intent");
            }
        }
    }
}
