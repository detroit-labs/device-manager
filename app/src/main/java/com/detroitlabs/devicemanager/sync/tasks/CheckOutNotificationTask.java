package com.detroitlabs.devicemanager.sync.tasks;

import android.content.Context;

import com.detroitlabs.devicemanager.di.qualifiers.ApplicationContext;

import javax.inject.Inject;

import static com.detroitlabs.devicemanager.sync.DeviceUpdateService.ACTION_CHECK_OUT;

public class CheckOutNotificationTask extends NotificationTask {

    @Inject
    public CheckOutNotificationTask(@ApplicationContext Context context) {
        super(context);
    }

    @Override
    protected CharSequence getActionTitle() {
        return "Check Out";
    }

    @Override
    protected String getDeviceUpdateIntentAction() {
        return ACTION_CHECK_OUT;
    }

    @Override
    protected CharSequence getContentText() {
        return "Check out to borrow this phone";
    }
}
