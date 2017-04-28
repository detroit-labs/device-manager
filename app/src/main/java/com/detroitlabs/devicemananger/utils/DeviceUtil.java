package com.detroitlabs.devicemananger.utils;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.detroitlabs.devicemananger.constants.Platform;
import com.detroitlabs.devicemananger.models.Device;
import com.jaredrummler.android.device.DeviceName;

import java.util.Locale;

public class DeviceUtil {
    public static Device getDeviceDetail(Context context) {
        Device detail = new Device();
        detail.platform = Platform.ANDROID;
        detail.brandAndModel = getBrandAndModel();
        detail.version = Build.VERSION.RELEASE;
        detail.screenResolution = getResolution(context);
        detail.screenSize = getSize(context);
        detail.serialNumber = getSerialNumber();
        return detail;
    }

    private static String getSerialNumber() {
        return Build.SERIAL;
    }

    private static String getSize(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(dm);
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
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        return width + " × " + height + " px";
    }

    private static String getBrandAndModel() {
        return DeviceName.getDeviceName();
    }
}
