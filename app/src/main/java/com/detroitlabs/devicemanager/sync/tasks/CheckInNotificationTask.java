package com.detroitlabs.devicemanager.sync.tasks;

import android.content.Context;

import com.detroitlabs.devicemanager.di.qualifiers.ApplicationContext;

import javax.inject.Inject;

import static com.detroitlabs.devicemanager.sync.DeviceUpdateService.ACTION_CHECK_IN;

public class CheckInNotificationTask extends NotificationTask {

    @Inject
    public CheckInNotificationTask(@ApplicationContext Context context) {
        super(context);
    }

    @Override
    protected CharSequence getActionTitle() {
        return "Check In";
    }

    @Override
    protected String getDeviceUpdateIntentAction() {
        return ACTION_CHECK_IN;
    }

    @Override
    protected CharSequence getContentText() {
        return "Check in before shutting down and returning the phone";
    }
}
