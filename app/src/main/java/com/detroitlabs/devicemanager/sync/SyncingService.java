package com.detroitlabs.devicemanager.sync;


import android.app.Service;
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

import com.detroitlabs.devicemanager.DmApplication;
import com.detroitlabs.devicemanager.constants.Constants;
import com.detroitlabs.devicemanager.db.Device;
import com.detroitlabs.devicemanager.db.DeviceDao;
import com.detroitlabs.devicemanager.repository.DeviceRepository;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import javax.inject.Inject;

import static com.detroitlabs.devicemanager.data.DatabaseContract.TABLE_DEVICES;

public class SyncingService extends Service {
    private static final String TAG = SyncingService.class.getSimpleName();
    private static final int ACTION_UNREGISTER = 13;
    private static final int ACTION_SYNC = 17;
    private static final String ACTION = TAG + ".ACTION";

    private ChildEventListener childEventListener;
    private ServiceHandler serviceHandler;

    @Inject
    DeviceRepository deviceRepo;

    public SyncingService() {
        super();
        DmApplication.getInjector().inject(this);
    }

    public static void initSync(Context context) {
        Intent intent = new Intent(context, SyncingService.class);
        intent.putExtra(ACTION, ACTION_SYNC);
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

    // TODO: 5/8/17 handle rotation change
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service hashcode: " + this.hashCode());
        int action = intent.getIntExtra(ACTION, 0);
        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = action;
        serviceHandler.sendMessage(msg);
        return super.onStartCommand(intent, flags, startId);
    }

    private void performSyncing() {
        DatabaseReference tableDevice = FirebaseDatabase.getInstance().getReference()
                .child(TABLE_DEVICES);
        int rowsDeleted = deviceRepo.emptyDeviceTable();
        Log.d(TAG, rowsDeleted + " rows cleared in devices table");
        tableDevice.addChildEventListener(getChildEventListener());
        notifyWhenDone(tableDevice);
    }

    private void notifyWhenDone(DatabaseReference tableDevice) {

        tableDevice.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "Sync is done, rows inserted: " + dataSnapshot.getChildrenCount());
                notifySyncDone();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void performUnregisterSyncing() {
        FirebaseDatabase.getInstance().getReference()
                .child(TABLE_DEVICES)
                .removeEventListener(getChildEventListener());
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

    private void notifySyncDone() {
        Intent localIntent = new Intent(Constants.BROADCAST_ACTION_SYNC_RESULT);
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
                case ACTION_UNREGISTER:
                    performUnregisterSyncing();
                    stopSelf();
                    break;
                case ACTION_SYNC:
                    performSyncing();
                    break;
                default:
                    throw new IllegalStateException("Illegal intent");
            }
        }
    }
}
