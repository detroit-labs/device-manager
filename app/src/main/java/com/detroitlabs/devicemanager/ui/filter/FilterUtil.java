package com.detroitlabs.devicemanager.ui.filter;


import android.util.Pair;

import com.detroitlabs.devicemanager.constants.FilterType;
import com.detroitlabs.devicemanager.ui.filter.adapters.FilterOptionAdapter;
import com.detroitlabs.devicemanager.ui.filter.adapters.IsSamsungAdapter;
import com.detroitlabs.devicemanager.ui.filter.adapters.PlatformFilterAdapter;
import com.detroitlabs.devicemanager.ui.filter.adapters.ScreenResolutionAdapter;
import com.detroitlabs.devicemanager.ui.filter.adapters.ScreenSizeAdapter;
import com.detroitlabs.devicemanager.ui.filter.adapters.VersionFilterAdapter;
import com.detroitlabs.devicemanager.ui.filter.adapters.YearClassAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FilterUtil {
    static FilterOptionAdapter[] createAdapters() {
        return new FilterOptionAdapter[]{
                new PlatformFilterAdapter(),
                new VersionFilterAdapter(),
                new ScreenSizeAdapter(),
                new ScreenResolutionAdapter(),
                new YearClassAdapter(),
                new IsSamsungAdapter()
        };
    }

    public static Pair<String, Object[]> convertSelectionToQuery(Filter.Selection selection) {
        StringBuilder qb = new StringBuilder("SELECT * FROM DEVICE");
        if (selection.hasSelection()) {
            qb.append(" WHERE ");
        }
        List<Object> args = new ArrayList<>();
        List<FilterType> filterTypes = selection.getSelectionKeys();
        for (int i = 0; i < filterTypes.size(); ++i) {
            if (i > 0) {
                qb.append(" AND ");
            }
            FilterType type = filterTypes.get(i);
            Set<String> values = selection.getSelectionValues(type);
            qb.append(type.toString());
            qb.append(" IN (");
            qb.append(getPlaceHolders(values.size()));
            qb.append(")");
            args.addAll(values);
        }
        return Pair.create(qb.toString(), args.toArray());
    }

    private static String getPlaceHolders(int count) {
        // ?,?,?,?,?
        String result = "";
        for (int i = 0; i < count; ++i) {
            if (i > 0) {
                result += ",";
            }
            result += "?";
        }
        return result;
    }
}
