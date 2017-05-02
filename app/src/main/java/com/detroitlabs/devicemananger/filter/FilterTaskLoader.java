package com.detroitlabs.devicemananger.filter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import com.detroitlabs.devicemananger.constants.FilterType;
import com.detroitlabs.devicemananger.data.DatabaseContract;
import com.detroitlabs.devicemananger.models.Filter;

import java.util.Set;


public class FilterTaskLoader extends AsyncTaskLoader<Filter> {
    private final Context context;
    private Filter filter;

    public FilterTaskLoader(Context context, Filter filter) {
        super(context);
        this.context = context;
        this.filter = filter;
    }

    // TODO: 5/1/17 handling onCancel triggered by restartLoader

    @Override
    protected void onStartLoading() {
        if (filter != null) {
            deliverResult(filter);
        } else {
            forceLoad();
        }
    }

    @Override
    public void deliverResult(Filter data) {
        if (isReset()) {
            return;
        }
        filter = data;
        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    public Filter loadInBackground() {
        Cursor cursor = context.getContentResolver().query(DatabaseContract.FILTER_URI, getProjection(), getSelection(), null, null);
        Filter filter = new Filter();
        try {
            // iterate cursor and read the options

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return filter;
    }

    private String[] getProjection() {
        if (filter.options == null || filter.options.isEmpty()) {
            return null;
        } else {
            Set<FilterType> filterTypes = filter.options.keySet();
            String[] projection = new String[filterTypes.size()];
            int index = 0;
            for (FilterType filterType : filterTypes) {
                projection[index] = filterType.toString();
                index++;
            }
            return projection;
        }
    }

    private String getSelection() {
        if (filter.selections == null || filter.selections.isEmpty()) {
            return null;
        } else {
            // filterType1 in (value1, value2) and filterType2 in (value1, value2)
            Set<FilterType> filterTypes = filter.selections.keySet();
            String selection = "";
            int index = 0;
            for (FilterType filterType : filterTypes) {
                if (index > 0) {
                    selection += " and ";
                }
                selection += filterType.toString() + " in (";
                selection += getArgs(filter.selections.get(filterType));
                selection += ")";
                index++;
            }
            return selection;
        }
    }

    private String getArgs(Set<String> values) {
        // value1, value2, value3
        String args = "";
        int index = 0;
        for (String s : values) {
            if (index > 0) {
                args += ",";
            }
            args += s;
            index++;
        }
        return args;
    }

    public void setFilterSelection() {
        // TODO: 5/1/17  
    }
}
