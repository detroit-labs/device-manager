package com.detroitlabs.devicemanager.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.detroitlabs.devicemanager.constants.Platform;

import java.util.List;

@Dao
public interface DeviceDao {
    @Insert
    void insertAll(Device... devices);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Device device);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Device device);

    @Delete
    void delete(Device... devices);

    @Query("DELETE FROM device")
    int emptyDeviceTable();

    @Query("SELECT * FROM device WHERE serialNumber <> :thisSerialNumber")
    LiveData<List<Device>> getAllExceptThis(String thisSerialNumber);

    @Query("SELECT * FROM device WHERE serialNumber = :serialNumber")
    LiveData<Device> getDevice(String serialNumber);

    @Query("SELECT * FROM device WHERE platform in (:platform) AND brandAndModel in (:brandAndModel) AND version in (:version) AND screenSize in (:screenSize) AND screenResolution in (:screenResolution) AND yearClass in (:yearClass) AND isSamsung in (:isSamsung) AND serialNumber <> :thisSerialNumber")
    LiveData<List<Device>> filterDevice(List<Platform> platform, List<String> brandAndModel, List<String> version, List<String> screenSize, List<String> screenResolution, List<String> yearClass, List<String> isSamsung, String thisSerialNumber);
}
