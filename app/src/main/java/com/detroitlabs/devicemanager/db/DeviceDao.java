package com.detroitlabs.devicemanager.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

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

    @Query("SELECT * FROM device")
    LiveData<List<Device>> getAllDevices();

    @Query("SELECT * FROM device WHERE serialNumber = :serialNumber")
    LiveData<Device> getDevice(String serialNumber);
}