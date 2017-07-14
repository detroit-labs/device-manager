package com.detroitlabs.devicemanager.di;


import android.app.Application;

import com.detroitlabs.devicemanager.sync.RegistrationService;
import com.detroitlabs.devicemanager.sync.SyncingService;
import com.detroitlabs.devicemanager.ui.DeviceListViewModel;
import com.detroitlabs.devicemanager.ui.HomeViewModel;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();

    }

    void inject(SyncingService syncingService);

    void inject(RegistrationService registrationService);

    void inject(HomeViewModel homeViewModel);

    void inject(DeviceListViewModel deviceListViewModel);
}
