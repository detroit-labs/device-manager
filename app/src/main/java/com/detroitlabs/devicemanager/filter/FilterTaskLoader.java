package com.detroitlabs.devicemanager.filter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import com.detroitlabs.devicemanager.constants.FilterType;
import com.detroitlabs.devicemanager.data.DatabaseContract;
import com.detroitlabs.devicemanager.models.Filter;


public class FilterTaskLoader extends AsyncTaskLoader<Filter.Options> {
    private final Context context;
    private Filter.Options filterOptions;

    public FilterTaskLoader(Context context) {
        super(context);
        this.context = context;
    }

    // TODO: 5/1/17 handling onCancel triggered by restartLoader

    @Override
    protected void onStartLoading() {
        if (filterOptions != null) {
            deliverResult(filterOptions);
        } else {
            forceLoad();
        }
    }

    @Override
    public void deliverResult(Filter.Options data) {
        if (isReset()) {
            return;
        }
        filterOptions = data;
        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    public Filter.Options loadInBackground() {
        Cursor cursor = context.getContentResolver().query(DatabaseContract.FILTER_URI,
                getProjection(),
                FilterUtil.convertFilterToQuerySelection(),
                null,
                null);
        Filter.Options filterOptions = new Filter.Options();
        try {
            if (cursor != null && cursor.moveToFirst()) {
                FilterType[] filterTypes = FilterType.values();
                do {
                    for (FilterType filterType : filterTypes) {
                        String optionValue = DatabaseContract.getString(cursor, filterType.toString());
                        filterOptions.addOptionValues(filterType, optionValue);
                    }
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return filterOptions;
    }

    private String[] getProjection() {
        FilterType[] filterTypes = FilterType.values();
        String[] projection = new String[filterTypes.length];
        for (int i = 0; i < filterTypes.length; ++i) {
            projection[i] = filterTypes[i].toString();
        }
        return projection;
    }

    public void setFilterSelection() {
        // TODO: 5/1/17  
    }
}
