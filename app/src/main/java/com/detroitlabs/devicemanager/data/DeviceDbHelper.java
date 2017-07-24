package com.detroitlabs.devicemanager.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.detroitlabs.devicemanager.data.DatabaseContract.DeviceColumns;

public class DeviceDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "devices.db";
    private static final String SQL_CREATE_TABLE_DEVICES = String.format("CREATE TABLE %s"
                    + " (%s TEXT PRIMARY KEY, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
            DatabaseContract.TABLE_DEVICES,
            DeviceColumns.SERIAL_NUMBER,
            DeviceColumns.VERSION,
            DeviceColumns.PLATFORM,
            DeviceColumns.IS_SAMSUNG,
            DeviceColumns.YEAR_CLASS,
            DeviceColumns.SCREEN_SIZE,
            DeviceColumns.REQUESTED_BY,
            DeviceColumns.CHECKED_OUT_BY,
            DeviceColumns.BRAND_AND_MODEL,
            DeviceColumns.SCREEN_RESOLUTION
    );

    public DeviceDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_DEVICES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + DatabaseContract.TABLE_DEVICES);
        onCreate(db);
    }
}
