package com.detroitlabs.devicemanager.db;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FilterDao {
    private final DeviceDatabase db;
    // TODO: 7/18/17 should Dao class maintain a LiveData?
    private final MutableLiveData<List<Device>> liveData;

    @Inject
    public FilterDao(DeviceDatabase db) {
        this.db = db;
        liveData = new MutableLiveData<>();
    }

    public LiveData<List<Device>> getFilteredDevices(String query, Object[] args) {
        updateFilterQuery(query, args);
        return liveData;
    }

    public void updateFilterQuery(String query, Object[] args) {
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
            final int _cursorIndexOfNotRegisterable = cursor.getColumnIndexOrThrow("notRegisterable");
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
                final int _tmp_1 = cursor.getInt(_cursorIndexOfNotRegisterable);
                device.notRegisterable = _tmp_1 != 0;
                _result.add(device);
            }
            liveData.setValue(_result);
        } finally {
            cursor.close();
        }
    }
}
