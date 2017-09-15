package com.detroitlabs.devicemanager.di;


import android.support.v4.app.FragmentActivity;

import com.detroitlabs.devicemanager.PagerActivity;
import com.detroitlabs.devicemanager.SyncActivity;
import com.detroitlabs.devicemanager.di.scopes.PerActivity;
import com.detroitlabs.devicemanager.sync.Ui;
import com.google.android.gms.common.api.GoogleApiClient;

import dagger.BindsInstance;
import dagger.Component;

@PerActivity
@Component(modules = {GoogleApiModule.class, ActivityModule.class},
        dependencies = {AppComponent.class})
public interface ActivityComponent {

    @Component.Builder
    interface Builder {

        Builder appComponent(AppComponent appComponent);

        @BindsInstance
        Builder fragmentActivity(FragmentActivity fragmentActivity);

        @BindsInstance
        Builder listener(GoogleApiClient.OnConnectionFailedListener listener);

        @BindsInstance
        Builder ui(Ui ui);

        ActivityComponent build();
    }

    void inject(SyncActivity syncActivity);

    void inject(PagerActivity pagerActivity);
}