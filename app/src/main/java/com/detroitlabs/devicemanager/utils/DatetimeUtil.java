package com.detroitlabs.devicemanager.utils;



import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatetimeUtil {
    public static String formatDate(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm MM/dd", Locale.getDefault());
        return sdf.format(date);
    }
    public static String formatDate(Long date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm MM/dd", Locale.getDefault());
        return sdf.format(new Date(date));
    }

    public static long getCurrentTimeInMillis() {
        return System.currentTimeMillis();
    }
}
