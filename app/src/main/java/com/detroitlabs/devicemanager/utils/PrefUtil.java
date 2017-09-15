package com.detroitlabs.devicemanager.utils;

import android.content.Context;
import android.preference.PreferenceManager;

import com.detroitlabs.devicemanager.di.qualifiers.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.detroitlabs.devicemanager.constants.Constants.PREF_IS_REGISTRABLE;
import static com.detroitlabs.devicemanager.constants.Constants.PREF_REGISTER_SUCCEEDED;

@Singleton
public class PrefUtil {

    private final Context context;

    @Inject
    public PrefUtil(@ApplicationContext Context context) {
        this.context = context;
    }

    public static void setRegisterSuccessful(Context context, boolean isSuccessful) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putBoolean(PREF_REGISTER_SUCCEEDED, isSuccessful).apply();
    }

    public void saveIsRegistrable(boolean isRegistrable) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_IS_REGISTRABLE, isRegistrable)
                .apply();
    }

    public boolean isRegistrable() {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_IS_REGISTRABLE, false);
    }
}
