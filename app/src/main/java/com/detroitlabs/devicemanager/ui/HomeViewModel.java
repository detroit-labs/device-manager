package com.detroitlabs.devicemanager.ui;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.databinding.ObservableField;

import com.detroitlabs.devicemanager.DmApplication;
import com.detroitlabs.devicemanager.data.DeviceUpdateService;
import com.detroitlabs.devicemanager.db.Device;
import com.detroitlabs.devicemanager.di.DaggerAppComponent;
import com.detroitlabs.devicemanager.repository.DeviceRepository;

import javax.inject.Inject;

public class HomeViewModel extends AndroidViewModel {

    private final LiveData<Device> self;
    public final ObservableField<String> name = new ObservableField<>();
    @Inject
    DeviceRepository deviceRepo;

    public HomeViewModel(final Application application) {
        super(application);
        DmApplication.getInjector().inject(this);

        self = deviceRepo.getSelfDevice();
    }

    public LiveData<Device> getSelf() {
        return self;
    }

    public void checkOut() {
        DeviceUpdateService.checkOutDevice(getApplication(), name.get());
        name.set("");
    }

    public void dismissRequest() {
        DeviceUpdateService.requestDevice(this.getApplication(), DmApplication.getThisDevice().serialNumber, "");
    }

    public void checkIn() {
        DeviceUpdateService.checkInDevice(getApplication());
    }
}