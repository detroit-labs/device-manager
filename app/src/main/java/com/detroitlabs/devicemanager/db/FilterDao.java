package com.detroitlabs.devicemanager.db;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.InvalidationTracker;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FilterDao {
    private static final String TAG = FilterDao.class.getName();
    private final DeviceDatabase db;
    private final MutableLiveData<List<Device>> liveData;
    private InvalidationTracker.Observer observer;
    private String query;
    private Object[] args;

    @Inject
    public FilterDao(DeviceDatabase db) {
        this.db = db;
        liveData = new MutableLiveData<>();
    }

    public LiveData<List<Device>> getFilteredDevices(String query, Object[] args) {
        this.query = query;
        this.args = args;
        if (observer == null) {
            observer = createObserver();
            InvalidationTracker invalidationTracker = db.getInvalidationTracker();
            invalidationTracker.addObserver(observer);
        }
        updateFilterQuery(query, args);
        return liveData;
    }

    private void updateFilterQuery(String query, Object[] args) {
        Cursor cursor = db.query(query, args);
        try {
            final int _cursorIndexOfPlatform = cursor.getColumnIndexOrThrow("platform");
            final int _cursorIndexOfBrandAndModel = cursor.getColumnIndexOrThrow("brandAndModel");
            final int _cursorIndexOfVersion = cursor.getColumnIndexOrThrow("version");
            final int _cursorIndexOfScreenSize = cursor.getColumnIndexOrThrow("screenSize");
            final int _cursorIndexOfScreenResolution = cursor.getColumnIndexOrThrow("screenResolution");
            final int _cursorIndexOfSerialNumber = cursor.getColumnIndexOrThrow("serialNumber");
            final int _cursorIndexOfCheckedOutBy = cursor.getColumnIndexOrThrow("checkedOutBy");
            final int _cursorIndexOfRequestedBy = cursor.getColumnIndexOrThrow("requestedBy");
            final int _cursorIndexOfYearClass = cursor.getColumnIndexOrThrow("yearClass");
            final int _cursorIndexOfIsSamsung = cursor.getColumnIndexOrThrow("isSamsung");
            final List<Device> _result = new ArrayList<>(cursor.getCount());
            while (cursor.moveToNext()) {
                final Device device = new Device();
                final String _tmp = cursor.getString(_cursorIndexOfPlatform);
                device.platform = PlatformConverter.fromString(_tmp);
                device.brandAndModel = cursor.getString(_cursorIndexOfBrandAndModel);
                device.version = cursor.getString(_cursorIndexOfVersion);
                device.screenSize = cursor.getString(_cursorIndexOfScreenSize);
                device.screenResolution = cursor.getString(_cursorIndexOfScreenResolution);
                device.serialNumber = cursor.getString(_cursorIndexOfSerialNumber);
                device.checkedOutBy = cursor.getString(_cursorIndexOfCheckedOutBy);
                device.requestedBy = cursor.getString(_cursorIndexOfRequestedBy);
                device.yearClass = cursor.getString(_cursorIndexOfYearClass);
                device.isSamsung = cursor.getString(_cursorIndexOfIsSamsung);
                _result.add(device);
            }
            liveData.postValue(_result);
        } finally {
            cursor.close();
        }
    }

    private InvalidationTracker.Observer createObserver() {
        return new InvalidationTracker.Observer(new String[]{"device"}) {

            @Override
            public void onInvalidated(@NonNull Set<String> set) {
                Log.d(TAG, "the database is updated");
                updateFilterQuery(query, args);
            }
        };
    }
}
