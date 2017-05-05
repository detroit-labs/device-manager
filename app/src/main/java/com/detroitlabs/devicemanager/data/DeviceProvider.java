package com.detroitlabs.devicemanager.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.detroitlabs.devicemanager.data.DatabaseContract.CONTENT_AUTHORITY;
import static com.detroitlabs.devicemanager.data.DatabaseContract.DEVICE_URI;
import static com.detroitlabs.devicemanager.data.DatabaseContract.PATH_FILTERS;
import static com.detroitlabs.devicemanager.data.DatabaseContract.TABLE_DEVICES;


public class DeviceProvider extends ContentProvider {
    private static final int DEVICES = 1;
    private static final int DEVICE_FILTER = 2;
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    private static final String TAG = DeviceProvider.class.getSimpleName();

    static {
        URI_MATCHER.addURI(CONTENT_AUTHORITY,
                TABLE_DEVICES + "/" + PATH_FILTERS,
                DEVICE_FILTER);
        URI_MATCHER.addURI(CONTENT_AUTHORITY,
                TABLE_DEVICES,
                DEVICES);
    }

    private DeviceDbHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new DeviceDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseContract.TABLE_DEVICES, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (URI_MATCHER.match(uri)) {
            case DEVICES:
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                long id = db.insertWithOnConflict(TABLE_DEVICES, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                return ContentUris.withAppendedId(DatabaseContract.DEVICE_URI, id);
            default:
                throw new IllegalArgumentException("Illegal insert URI");
        }
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        Log.d(TAG, "Start bulk inserting data");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (ContentValues value : values) {
                String whereClause = String.format("%s = ?", DatabaseContract.DeviceColumns.SERIAL_NUMBER);
                String[] whereArgs = new String[]{value.getAsString(DatabaseContract.DeviceColumns.SERIAL_NUMBER)};
                int affectedId = db.update(TABLE_DEVICES, value, whereClause, whereArgs);
                if (affectedId == 0) {
                    db.insert(TABLE_DEVICES, null, value);
                }
            }
            db.setTransactionSuccessful();
        } catch (SQLException ex) {
            Log.w(TAG, ex);
        } finally {
            db.endTransaction();
        }
        return values.length;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = String.format("%s = ?", DatabaseContract.DeviceColumns.SERIAL_NUMBER);
        String[] whereArgs = new String[]{values.getAsString(DatabaseContract.DeviceColumns.SERIAL_NUMBER)};
        int affectedRows = db.update(TABLE_DEVICES, values, whereClause, whereArgs);
        if (affectedRows != 0) {
            getContext().getContentResolver().notifyChange(DEVICE_URI, null);
        }
        return affectedRows;
    }
}
