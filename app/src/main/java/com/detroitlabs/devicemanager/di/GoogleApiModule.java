package com.detroitlabs.devicemanager.di;


import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.detroitlabs.devicemanager.R;
import com.detroitlabs.devicemanager.di.qualifiers.ApplicationContext;
import com.detroitlabs.devicemanager.di.scopes.PerActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

import dagger.Module;
import dagger.Provides;

import static com.detroitlabs.devicemanager.constants.Constants.RESTRICTED_TEST_DEVICE_ACCOUNT;

@Module
public class GoogleApiModule {
    @PerActivity
    @Provides
    GoogleSignInOptions providesGoogleSignInOptions(@ApplicationContext Context context) {
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .setAccountName(RESTRICTED_TEST_DEVICE_ACCOUNT)
                .build();
    }

    @PerActivity
    @Provides
    GoogleApiClient providesGoogleApiClient(GoogleSignInOptions gso,
                                            FragmentActivity fragmentActivity,
                                            @ApplicationContext Context context,
                                            GoogleApiClient.OnConnectionFailedListener listener) {
        return new GoogleApiClient.Builder(context)
                .enableAutoManage(fragmentActivity, listener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }
}
