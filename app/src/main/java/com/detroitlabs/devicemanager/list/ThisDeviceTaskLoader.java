package com.detroitlabs.devicemanager.list;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;

import com.detroitlabs.devicemanager.data.DatabaseContract;
import com.detroitlabs.devicemanager.models.Device;
import com.detroitlabs.devicemanager.utils.DeviceUtil;

import static com.detroitlabs.devicemanager.data.DatabaseContract.DEVICE_URI;


public class ThisDeviceTaskLoader extends AsyncTaskLoader<Device> {
    private final Context context;

    public ThisDeviceTaskLoader(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public Device loadInBackground() {
        Cursor cursor = context.getContentResolver().query(DEVICE_URI,
                null,
                String.format("%s=?", DatabaseContract.DeviceColumns.SERIAL_NUMBER),
                new String[]{DeviceUtil.getSerialNumber()},
                null);
        try {
            if (cursor != null) {
                cursor.moveToFirst();
                return new Device(cursor);
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        throw new IllegalStateException("No THIS DEVICE found");
    }

    @Override
    public void deliverResult(Device device) {
        if (isReset()) {
            return;
        }
//        THIS_DEVICE = device;
    }
}
