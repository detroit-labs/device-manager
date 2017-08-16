package com.detroitlabs.devicemanager.repository;


import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.detroitlabs.devicemanager.DmApplication;
import com.detroitlabs.devicemanager.constants.FilterType;
import com.detroitlabs.devicemanager.db.Device;
import com.detroitlabs.devicemanager.db.DeviceDao;
import com.detroitlabs.devicemanager.db.FilterDao;
import com.detroitlabs.devicemanager.ui.filter.Filter;
import com.detroitlabs.devicemanager.ui.filter.FilterUtil;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DeviceRepository {

    private static final String TAG = DeviceRepository.class.getName();
    private final DeviceDao deviceDao;
    private final FilterDao filterDao;

    @Inject
    public DeviceRepository(DeviceDao deviceDao, FilterDao filterDao) {
        this.deviceDao = deviceDao;
        this.filterDao = filterDao;
    }

    public LiveData<Device> getSelfDevice() {
        return deviceDao.getDevice(DmApplication.getSerialNumber());
    }

    public void insert(Device device) {
        if (!hasEnoughProperty(device)) {
            return;
        }
        new AsyncTask<Device, Void, Void>() {
            @Override
            protected Void doInBackground(Device... devices) {
                deviceDao.insert(devices[0]);
                return null;
            }
        }.execute(device);
    }

    public int emptyDeviceTable() {
        return deviceDao.emptyDeviceTable();
    }

    public void delete(Device device) {
        deviceDao.delete(device);
    }

    public void update(Device device) {
        new AsyncTask<Device, Void, Void>() {
            @Override
            protected Void doInBackground(Device... devices) {
                deviceDao.update(devices[0]);
                return null;
            }
        }.execute(device);
    }

    public LiveData<List<Device>> getAllDevices(Filter.Selection selection) {
        Pair<String, Object[]> queryArg = FilterUtil.convertSelectionToQuery(selection);
        return filterDao.getFilteredDevices(queryArg.first, queryArg.second);
    }

    public void refreshList(Filter.Selection selections) {
        Pair<String, Object[]> queryArg = FilterUtil.convertSelectionToQuery(selections);
        filterDao.updateFilterQuery(queryArg.first, queryArg.second);
    }

    public LiveData<Filter.Options> loadAllFilterOptions(Filter.Selection selection) {
        return loadAllFilterOptions(getAllDevices(selection));
    }

    public LiveData<Filter.Options> loadAllFilterOptions() {
        return loadAllFilterOptions(deviceDao.getAllDevices());
    }

    private LiveData<Filter.Options> loadAllFilterOptions(LiveData<List<Device>> deviceLiveData) {
        return Transformations.map(deviceLiveData, new Function<List<Device>, Filter.Options>() {
            @Override
            public Filter.Options apply(List<Device> devices) {
                Log.d(TAG, "transform device list to filter options");
                Filter.Options allOptions = new Filter.Options();
                FilterType[] filterTypes = FilterType.values();
                for (FilterType type : filterTypes) {
                    for (Device device : devices) {
                        allOptions.addOptionValues(type, device.getFilterValue(type));
                    }
                }
                return allOptions;
            }
        });
    }

    private boolean hasEnoughProperty(Device device) {
        return device.brandAndModel != null && device.platform != null &&
                device.screenSize != null && device.screenResolution != null &&
                device.version != null && device.serialNumber != null;
    }
}
