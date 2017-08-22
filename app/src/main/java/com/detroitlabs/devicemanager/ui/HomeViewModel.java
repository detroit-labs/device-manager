package com.detroitlabs.devicemanager.ui;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.databinding.Observable;
import android.databinding.ObservableField;

import com.detroitlabs.devicemanager.DmApplication;
import com.detroitlabs.devicemanager.sync.DeviceUpdateService;
import com.detroitlabs.devicemanager.db.Device;
import com.detroitlabs.devicemanager.repository.DeviceRepository;

import javax.inject.Inject;

public class HomeViewModel extends AndroidViewModel {

    private final LiveData<Device> self;
    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableField<Boolean> enabled = new ObservableField<>();

    @Inject
    DeviceRepository deviceRepo;

    public HomeViewModel(final Application application) {
        super(application);
        DmApplication.getInjector().inject(this);
        self = deviceRepo.getSelfDevice();
        name.addOnPropertyChangedCallback(getNameChangedCallback());
    }

    private Observable.OnPropertyChangedCallback getNameChangedCallback() {
        return new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (sender == name) {
                    enabled.set(name.get() != null && !name.get().isEmpty());
                }
            }
        };
    }

    public LiveData<Device> getSelf() {
        return self;
    }

    public void checkOut() {
        DeviceUpdateService.checkOutDevice(getApplication(), name.get());
        name.set("");
    }

    public void dismissRequest() {
        DeviceUpdateService.requestDevice(getApplication(), DmApplication.getSerialNumber(), "");
    }

    public void checkIn() {
        DeviceUpdateService.checkInDevice(getApplication());
    }
}