package com.detroitlabs.devicemananger.data;

import android.content.Context;
import android.database.Cursor;

/**
 * Created by peike on 5/1/17.
 */

public class FilterUpdateService {
    public Cursor queryFilter(Context context) {
        context.getContentResolver().query(DatabaseContract.FILTER_URI, )
    }
}
