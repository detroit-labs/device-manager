package com.detroitlabs.devicemanager.filter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import com.detroitlabs.devicemanager.constants.FilterType;
import com.detroitlabs.devicemanager.data.DatabaseContract;
import com.detroitlabs.devicemanager.models.Filter;

import java.util.List;


public class FilterTaskLoader extends AsyncTaskLoader<Filter.Options> {
    private final Context context;
    private Filter.Options filterOptions;
    private Filter.Selection filterSelection;

    public FilterTaskLoader(Context context, Filter.Selection filterSelection) {
        super(context);
        this.context = context;
        this.filterSelection = filterSelection;
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
        Cursor cursor = context.getContentResolver().query(DatabaseContract.FILTER_URI, getProjection(), getSelection(), null, null);
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

    private String getSelection() {
        if (!filterSelection.hasSelection()) {
            return null;
        } else {
            // filterType1 in ('value1', 'value2') and filterType2 in ('value1', 'value2')
            String selection = "";
            List<String> selectionTypes = filterSelection.getSelectionKeys();
            for (int i = 0; i < selectionTypes.size(); ++i) {
                if (i > 0) {
                    selection += " and ";
                }
                String selectionType = selectionTypes.get(i);
                selection += selectionType + " in (";
                selection += getArgs(filterSelection.getSelectionValues(selectionType));
                selection += ")";

            }
            return selection;
        }
    }

    private String getArgs(List<String> values) {
        // 'value1', 'value2', 'value3'
        String args = "";
        for (int index = 0; index < values.size(); ++index) {
            if (index > 0) {
                args += ",";
            }
            args += "'";
            args += values.get(index);
            args += "'";
        }
        return args;
    }

    public void setFilterSelection() {
        // TODO: 5/1/17  
    }
}
