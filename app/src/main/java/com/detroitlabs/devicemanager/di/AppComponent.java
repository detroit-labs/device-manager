package com.detroitlabs.devicemanager.di;


import android.app.Application;
import android.content.Context;

import com.detroitlabs.devicemanager.db.DeviceDao;
import com.detroitlabs.devicemanager.db.DeviceDatabase;
import com.detroitlabs.devicemanager.db.FilterDao;
import com.detroitlabs.devicemanager.di.qualifiers.ApplicationContext;
import com.detroitlabs.devicemanager.repository.DeviceRepository;
import com.detroitlabs.devicemanager.sync.RegistrationService;
import com.detroitlabs.devicemanager.sync.SyncingService;
import com.detroitlabs.devicemanager.ui.DeviceListViewModel;
import com.detroitlabs.devicemanager.ui.HomeViewModel;
import com.detroitlabs.devicemanager.ui.filter.FilterViewModel;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {DatabaseModule.class, AppModule.class})
public interface AppComponent {


    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();

    }

    @ApplicationContext
    Context applicationContext();

    DeviceDao deviceDao();

    DeviceDatabase deviceDatabase();

    DeviceRepository deviceRepository();

    FilterDao filterDao();

    void inject(SyncingService syncingService);

    void inject(RegistrationService registrationService);

    void inject(HomeViewModel homeViewModel);

    void inject(DeviceListViewModel deviceListViewModel);

    void inject(FilterViewModel filterViewModel);
}
