package com.detroitlabs.devicemanager.list;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.detroitlabs.devicemanager.data.DatabaseContract;
import com.detroitlabs.devicemanager.filter.FilterUtil;
import com.detroitlabs.devicemanager.models.Device;


/*
 * Copied most of the code from android.content.CursorLoader
 * */
class ThisDeviceTaskLoader extends AsyncTaskLoader<Device> {
    private final Context context;
    private Cursor cursor;
    private Device device;
    private ForceLoadContentObserver observer;

    ThisDeviceTaskLoader(Context context) {
        super(context);
        this.context = context;
        observer = new ForceLoadContentObserver();
    }

    @Override
    @Nullable
    public Device loadInBackground() {
        Device device = null;
        Cursor cursor = context.getContentResolver().query(
                DatabaseContract.THIS_DEVICE_URI,
                null,
                FilterUtil.getThisDeviceSelection(),
                null,
                null
        );
        if (cursor != null && cursor.moveToFirst()) {
            if (this.cursor != null && this.cursor != cursor && !this.cursor.isClosed()) {
                this.cursor.close();
            }
            this.cursor = cursor;
            cursor.registerContentObserver(observer);
            device = new Device(cursor);
        }
        return device;
    }

    @Override
    protected void onStartLoading() {
        if (device != null) {
            deliverResult(device);
        }
        if (takeContentChanged() || device == null) {
            forceLoad();
        }
    }

    @Override
    public void deliverResult(Device data) {
        if (isReset()) {
            if (cursor != null) {
                cursor.close();
            }
            return;
        }
        device = data;
        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    /**
     * Must be called from the UI thread
     */
    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    @Override
    public void onCanceled(Device device) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    @Override
    protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        cursor = null;
    }
}
