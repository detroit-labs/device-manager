package com.detroitlabs.devicemananger.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.detroitlabs.devicemananger.data.DatabaseContract.CONTENT_AUTHORITY;
import static com.detroitlabs.devicemananger.data.DatabaseContract.PATH_FILTERS;
import static com.detroitlabs.devicemananger.data.DatabaseContract.TABLE_DEVICES;


public class DeviceProvider extends ContentProvider {
    private static final int DEVICES = 1;
    private static final int DEVICE_FILTER = 2;
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

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
        boolean distinct = false;
        switch (URI_MATCHER.match(uri)) {
            case DEVICES:
                break;
            case DEVICE_FILTER:
                distinct = true;
                break;
            default:
                throw new IllegalArgumentException("Illegal query URI");
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(distinct, DatabaseContract.TABLE_DEVICES, projection, selection, selectionArgs, null, null, sortOrder, null);
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
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
