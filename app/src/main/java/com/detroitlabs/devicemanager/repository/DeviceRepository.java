package com.detroitlabs.devicemanager.repository;


import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.detroitlabs.devicemanager.DmApplication;
import com.detroitlabs.devicemanager.db.Device;
import com.detroitlabs.devicemanager.db.DeviceDao;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DeviceRepository {

    private final DeviceDao deviceDao;

    @Inject
    public DeviceRepository(DeviceDao deviceDao) {
        this.deviceDao = deviceDao;
    }

    public LiveData<Device> getSelfDevice() {
        return deviceDao.getDevice(DmApplication.getThisDevice().serialNumber);
    }

    public void insert(Device device) {
        new AsyncTask<Device, Void, Void>() {
            @Override
            protected Void doInBackground(Device... devices) {
                deviceDao.insert(devices[0]);
                return null;
            }
        }.execute(device);
    }

    public int emptyDeviceTable() {
        return deviceDao.emptyDeviceTable();
    }

    public void delete(Device device) {
        deviceDao.delete(device);
    }

    public void update(Device device) {
        new AsyncTask<Device, Void, Void>() {
            @Override
            protected Void doInBackground(Device... devices) {
                deviceDao.update(devices[0]);
                return null;
            }
        }.execute(device);
    }
}
