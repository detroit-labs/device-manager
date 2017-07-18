package com.detroitlabs.devicemanager.ui;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.detroitlabs.devicemanager.DmApplication;
import com.detroitlabs.devicemanager.db.Device;
import com.detroitlabs.devicemanager.repository.DeviceRepository;
import com.detroitlabs.devicemanager.ui.filter.Filter;

import java.util.List;

import javax.inject.Inject;

public class DeviceListViewModel extends ViewModel {

    private final LiveData<List<Device>> deviceList;

    @Inject
    DeviceRepository deviceRepo;

    public DeviceListViewModel() {
        DmApplication.getInjector().inject(this);
        deviceList = deviceRepo.getAllDevices(new Filter.Selection());
    }

    LiveData<List<Device>> getDeviceList() {
        return deviceList;
    }
}
