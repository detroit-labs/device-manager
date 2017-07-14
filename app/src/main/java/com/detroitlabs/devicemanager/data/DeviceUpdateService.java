package com.detroitlabs.devicemanager.data;


import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.detroitlabs.devicemanager.utils.DeviceUtil;
import com.google.firebase.database.FirebaseDatabase;

import static com.detroitlabs.devicemanager.data.DatabaseContract.DEVICE_URI;
import static com.detroitlabs.devicemanager.data.DatabaseContract.TABLE_DEVICES;
import static com.detroitlabs.devicemanager.data.DatabaseContract.THIS_DEVICE_URI;

public class DeviceUpdateService extends IntentService {

    private static final String TAG = DeviceUpdateService.class.getSimpleName();
    private static final String ACTION = TAG + ".ACTION";
    private static final String EXTRA = TAG + ".EXTRA";
    private static final String ACTION_CHECK_IN = ACTION + ".CHECK_IN";
    private static final String ACTION_CHECK_OUT = ACTION + ".CHECK_OUT";
    private static final String ACTION_REQUEST = ACTION + ".REQUEST";
    private static final String ACTION_RECEIVE_REQUEST = ACTION + ".RECEIVE_REQUEST";
    private static final String EXTRA_SERIAL_NUMBER = EXTRA + ".SERIAL_NUMBER";
    private static final String EXTRA_CHECKED_OUT_BY = EXTRA + ".CHECKED_OUT_BY";
    private static final String EXTRA_REQUESTED_BY = EXTRA + ".REQUESTED_BY";

    public static void checkInDevice(Context context) {
        Intent intent = new Intent(context, DeviceUpdateService.class);
        intent.setAction(ACTION_CHECK_IN);
        intent.putExtra(EXTRA_SERIAL_NUMBER, DeviceUtil.getSerialNumber());
        context.startService(intent);
    }

    public static void checkOutDevice(Context context, String checkedOutBy) {
        Intent intent = new Intent(context, DeviceUpdateService.class);
        intent.setAction(ACTION_CHECK_OUT);
        intent.putExtra(EXTRA_SERIAL_NUMBER, DeviceUtil.getSerialNumber());
        intent.putExtra(EXTRA_CHECKED_OUT_BY, checkedOutBy);
        context.startService(intent);
    }

    public static void requestDevice(Context context, String serialNumber, String requestBy) {
        Intent intent = new Intent(context, DeviceUpdateService.class);
        intent.setAction(ACTION_REQUEST);
        intent.putExtra(EXTRA_SERIAL_NUMBER, serialNumber);
        intent.putExtra(EXTRA_REQUESTED_BY, requestBy);
        context.startService(intent);
    }

    public static void receiveRequest(Context context, String requestedBy) {
        Intent intent = new Intent(context, DeviceUpdateService.class);
        intent.setAction(ACTION_RECEIVE_REQUEST);
        intent.putExtra(EXTRA_SERIAL_NUMBER, DeviceUtil.getSerialNumber());
        intent.putExtra(EXTRA_REQUESTED_BY, requestedBy);
        context.startService(intent);
    }

    public DeviceUpdateService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String serialNumber = intent.getStringExtra(EXTRA_SERIAL_NUMBER);
        String action = intent.getAction();
        if (ACTION_CHECK_IN.equals(action)) {
            performCheckIn(serialNumber);
        } else if (ACTION_CHECK_OUT.equals(action)) {
            String checkedOutBy = intent.getStringExtra(EXTRA_CHECKED_OUT_BY);
            performCheckOut(serialNumber, checkedOutBy);
        } else if (ACTION_REQUEST.equals(action)) {
            String requestedBy = intent.getStringExtra(EXTRA_REQUESTED_BY);
            performRequest(serialNumber, requestedBy);
        } else if (ACTION_RECEIVE_REQUEST.equals(action)) {
            String requestedBy = intent.getStringExtra(EXTRA_REQUESTED_BY);
            performPersistRequest(serialNumber, requestedBy);
        }
    }

    private void performPersistRequest(String serialNumber, String requestedBy) {
        updateLocalDb(THIS_DEVICE_URI, serialNumber, DatabaseContract.DeviceColumns.REQUESTED_BY, requestedBy);
    }

    private void performCheckIn(String serialNumber) {
        FirebaseDatabase.getInstance().getReference()
                .child(TABLE_DEVICES)
                .child(serialNumber)
                .child(DatabaseContract.DeviceColumns.CHECKED_OUT_BY)
                .setValue("");
    }

    private void performCheckOut(String serialNumber, String checkedOutBy) {
        FirebaseDatabase.getInstance().getReference()
                .child(TABLE_DEVICES)
                .child(serialNumber)
                .child(DatabaseContract.DeviceColumns.CHECKED_OUT_BY)
                .setValue(checkedOutBy);
    }

    private void performRequest(String serialNumber, String requestedBy) {
        updateLocalDb(DEVICE_URI, serialNumber, DatabaseContract.DeviceColumns.REQUESTED_BY, requestedBy);
        FirebaseDatabase.getInstance().getReference()
                .child(TABLE_DEVICES)
                .child(serialNumber)
                .child(DatabaseContract.DeviceColumns.REQUESTED_BY)
                .setValue(requestedBy);
    }

    private void updateLocalDb(Uri uri, String serialNumber, String column, String value) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.DeviceColumns.SERIAL_NUMBER, serialNumber);
        values.put(column, value);
        getContentResolver().update(uri, values, null, null);
    }
}
