package com.detroitlabs.devicemanager.di;


import android.app.Application;
import android.content.Context;

import com.detroitlabs.devicemanager.NotificationActivity;
import com.detroitlabs.devicemanager.PowerOnOffReceiver;
import com.detroitlabs.devicemanager.db.DeviceDao;
import com.detroitlabs.devicemanager.db.DeviceDatabase;
import com.detroitlabs.devicemanager.db.FilterDao;
import com.detroitlabs.devicemanager.di.qualifiers.ApplicationContext;
import com.detroitlabs.devicemanager.notification.DmNotification;
import com.detroitlabs.devicemanager.repository.DeviceRepository;
import com.detroitlabs.devicemanager.sync.DeviceUpdateService;
import com.detroitlabs.devicemanager.sync.SelfDeviceUpdateService;
import com.detroitlabs.devicemanager.ui.DeviceListViewModel;
import com.detroitlabs.devicemanager.ui.HomeFragment;
import com.detroitlabs.devicemanager.ui.HomeViewModel;
import com.detroitlabs.devicemanager.ui.filter.FilterViewModel;
import com.detroitlabs.devicemanager.utils.PrefUtil;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {DatabaseModule.class, AppModule.class})
public interface AppComponent {

    void inject(HomeFragment homeFragment);

    void inject(SelfDeviceUpdateService selfDeviceUpdateService);

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

    PrefUtil prefUtil();

    DmNotification dmNotification();

    void inject(NotificationActivity notificationActivity);

    void inject(HomeViewModel homeViewModel);

    void inject(DeviceListViewModel deviceListViewModel);

    void inject(FilterViewModel filterViewModel);

    void inject(PowerOnOffReceiver powerOnOffReceiver);

    void inject(DeviceUpdateService deviceUpdateService);
}
