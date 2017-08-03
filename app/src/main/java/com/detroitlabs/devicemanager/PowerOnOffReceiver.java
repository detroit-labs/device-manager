package com.detroitlabs.devicemanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.detroitlabs.devicemanager.sync.DeviceUpdateService;
import com.detroitlabs.devicemanager.utils.DeviceUtil;


public class PowerOnOffReceiver extends BroadcastReceiver {
    private static final String TAG = PowerOnOffReceiver.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        float batteryPct = DeviceUtil.getBatteryLevel(context);
        Log.d(TAG, "Action: " + intent.getAction());
        Log.d(TAG, "Update battery percentage: " + batteryPct);
        // TODO: 7/27/17 check if logged in, if not, send notification
        DeviceUpdateService.updateLastKnownBattery(context, batteryPct);
    }
}
