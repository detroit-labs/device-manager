package com.detroitlabs.devicemanager.utils;

import android.content.Context;
import android.preference.PreferenceManager;

import static com.detroitlabs.devicemanager.constants.Constants.PREF_REGISTER_SUCCEEDED;


public class PrefUtil {
    public static void setRegisterSuccessful(Context context, boolean isSuccessful) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putBoolean(PREF_REGISTER_SUCCEEDED, isSuccessful).apply();
    }
}
