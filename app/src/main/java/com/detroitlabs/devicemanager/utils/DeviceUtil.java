package com.detroitlabs.devicemanager.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.detroitlabs.devicemanager.constants.Platform;
import com.detroitlabs.devicemanager.models.Device;
import com.jaredrummler.android.device.DeviceName;

import java.util.Locale;

public class DeviceUtil {

    private static Device THIS_DEVICE;

    public static Device getDevice() {
        return THIS_DEVICE;
    }

    public static boolean isCheckedOut() {
        return THIS_DEVICE.isCheckedOut();
    }

    public static void setCheckedOutBy(String checkedOutBy) {
        THIS_DEVICE.checkedOutBy = checkedOutBy;
    }

    public static boolean isThisDevice(String serialNumber) {
        return serialNumber != null && serialNumber.equals(THIS_DEVICE.serialNumber);
    }

    public static boolean hasGetAccountsPermission(Context context) {
        return ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED;
    }

    public static void requestGetAccountsPermission(Activity activity, int requestCode) {
        ActivityCompat.requestPermissions(activity,
                new String[]{android.Manifest.permission.GET_ACCOUNTS},
                requestCode);
    }

    public static void readThisDevice(Context context) {
        Device detail = new Device();
        detail.platform = Platform.ANDROID;
        detail.brandAndModel = getBrandAndModel();
        detail.version = Build.VERSION.RELEASE;
        detail.screenResolution = getResolution(context);
        detail.screenSize = getSize(context);
        detail.serialNumber = getSerialNumber();
        THIS_DEVICE = detail;
    }

    public static String getSerialNumber() {
        return Build.SERIAL;
    }

    private static String getSize(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getRealMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        double wi = (double) width / (double) dm.xdpi;
        double hi = (double) height / (double) dm.ydpi;
        double x = Math.pow(wi, 2);
        double y = Math.pow(hi, 2);
        double screenInches = Math.sqrt(x + y);
        return String.format(Locale.getDefault(), "%.1f in", screenInches);
    }

    private static String getResolution(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        int width = size.x;
        int height = size.y;
        return width + " × " + height + " px";
    }

    private static String getBrandAndModel() {
        return DeviceName.getDeviceName();
    }

    public static void updateDevice(@Nullable Device thisDevice) {
        if (thisDevice != null) {
            THIS_DEVICE.checkedOutBy = thisDevice.checkedOutBy;
            THIS_DEVICE.requestedBy = thisDevice.requestedBy;
        }
    }
}
