package com.detroitlabs.devicemanager.data;


import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.detroitlabs.devicemanager.utils.DeviceUtil;
import com.google.firebase.database.FirebaseDatabase;

import static com.detroitlabs.devicemanager.data.DatabaseContract.TABLE_DEVICES;

public class DeviceUpdateService extends IntentService {

    public static final String TAG = DeviceUpdateService.class.getSimpleName();

    public static final String ACTION_CHECK_IN = TAG + ".CHECK_IN";
    public static final String ACTION_CHECK_OUT = TAG + ".CHECK_OUT";
    private static final String EXTRA_SERIAL_NUMBER = TAG + ".SERIAL_NUMBER";
    private static final String EXTRA_CHECKED_OUT_BY = TAG + ".CHECKED_OUT_BY";

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

    public DeviceUpdateService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String serialNumber = intent.getStringExtra(EXTRA_SERIAL_NUMBER);
        if (ACTION_CHECK_IN.equals(intent.getAction())) {
            performCheckIn(serialNumber);
        } else if (ACTION_CHECK_OUT.equals(intent.getAction())) {
            String checkedOutBy = intent.getStringExtra(EXTRA_CHECKED_OUT_BY);
            performCheckOut(serialNumber, checkedOutBy);
        }
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

}
