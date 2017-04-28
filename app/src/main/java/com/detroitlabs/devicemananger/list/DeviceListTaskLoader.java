package com.detroitlabs.devicemananger.list;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.detroitlabs.devicemananger.constants.TestResponse;
import com.detroitlabs.devicemananger.models.Device;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;


public class DeviceListTaskLoader extends AsyncTaskLoader<List<Device>> {

    private static final String TAG = DeviceListTaskLoader.class.getSimpleName();
    public static final int LOADER_ID = 113;

    private List<Device> details;

    public DeviceListTaskLoader(Context context) {
        super(context);
    }

    @Override
    public List<Device> loadInBackground() {
        // call the api to get list of devices
        Moshi moshi = new Moshi.Builder().build();
        Type type = Types.newParameterizedType(List.class, Device.class);
        JsonAdapter<List<Device>> adapter = moshi.adapter(type);
        List<Device> detailList = null;
        try {
            detailList = adapter.fromJson(TestResponse.ALL_DEVICES);
        } catch (IOException e) {
            Log.e(TAG, "error parsing JSON from string", e);
        }
        return detailList;
    }

    @Override
    public void deliverResult(List<Device> data) {
        if (isReset()) {
            // An async query came in while the loader is stopped
            details = null;
            return;
        }
        details = data;

        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStartLoading() {
        if (details != null) {
            deliverResult(details);
        } else {
            forceLoad();
        }
    }
}
