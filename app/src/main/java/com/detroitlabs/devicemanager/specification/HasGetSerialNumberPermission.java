package com.detroitlabs.devicemanager.specification;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.detroitlabs.devicemanager.di.qualifiers.ApplicationContext;

import javax.inject.Inject;

public class HasGetSerialNumberPermission extends OreoOrHigher {

    private final Context context;

    @Inject
    public HasGetSerialNumberPermission(@ApplicationContext Context context) {
        this.context = context;
    }

    @Override
    public boolean isSatisfied() {
        return !super.isSatisfied() || hasPermission();
    }

    private boolean hasPermission() {
        int value = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
        return value == PackageManager.PERMISSION_GRANTED;
    }
}
