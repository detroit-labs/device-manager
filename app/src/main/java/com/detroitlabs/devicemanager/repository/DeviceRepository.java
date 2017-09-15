package com.detroitlabs.devicemanager.repository;


import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.detroitlabs.devicemanager.constants.FilterType;
import com.detroitlabs.devicemanager.db.Device;
import com.detroitlabs.devicemanager.db.DeviceDao;
import com.detroitlabs.devicemanager.db.FilterDao;
import com.detroitlabs.devicemanager.di.qualifiers.ApplicationContext;
import com.detroitlabs.devicemanager.sync.Result;
import com.detroitlabs.devicemanager.sync.tasks.DeviceCheckInTask;
import com.detroitlabs.devicemanager.sync.tasks.DeviceCheckOutTask;
import com.detroitlabs.devicemanager.ui.filter.Filter;
import com.detroitlabs.devicemanager.ui.filter.FilterUtil;
import com.detroitlabs.devicemanager.utils.DeviceUtil;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class DeviceRepository {

    private static final String TAG = DeviceRepository.class.getName();
    private final DeviceDao deviceDao;
    private final FilterDao filterDao;
    private final Context context;
    private final Provider<DeviceCheckOutTask> checkOutTaskProvider;
    private final Provider<DeviceCheckInTask> checkInTaskProvider;

    @Inject
    public DeviceRepository(DeviceDao deviceDao,
                            FilterDao filterDao,
                            @ApplicationContext Context context,
                            Provider<DeviceCheckOutTask> checkOutTaskProvider,
                            Provider<DeviceCheckInTask> checkInTaskProvider) {
        this.deviceDao = deviceDao;
        this.filterDao = filterDao;
        this.context = context;
        this.checkOutTaskProvider = checkOutTaskProvider;
        this.checkInTaskProvider = checkInTaskProvider;
    }

    public LiveData<Device> getSelfDevice() {
        return deviceDao.getDevice(DeviceUtil.getLocalSerialNumber(context));
    }

    public Single<Device> getSelfDeviceSingle() {
        return deviceDao.getDeviceSingle(DeviceUtil.getLocalSerialNumber(context));
    }

    public Single<Boolean> insert(final Device device) {
        if (!hasEnoughProperty(device)) {
            return Single.just(false);
        }
        return Single.create(new SingleOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Boolean> e) throws Exception {
                long rowId = deviceDao.insert(device);
                Log.d(TAG, "Insert done. Row Id: " + rowId);
                e.onSuccess(true);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public void delete(Device device) {
        new AsyncTask<Device, Void, Void>() {
            @Override
            protected Void doInBackground(Device... devices) {
                deviceDao.delete(devices[0]);
                return null;
            }
        }.execute(device);
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
        filterDao.getFilteredDevices(queryArg.first, queryArg.second);
    }

    public LiveData<Filter.Options> loadAllFilterOptions(Filter.Selection selection) {
        return loadAllFilterOptions(getAllDevices(selection));
    }

    public LiveData<Filter.Options> loadAllFilterOptions() {
        return loadAllFilterOptions(deviceDao.getAllDevices());
    }

    public Single<Result> checkOutDevice(String name) {
        DeviceCheckOutTask checkOutSequence = checkOutTaskProvider.get();
        return checkOutSequence.run(name);
    }

    public Single<Result> checkInDevice() {
        DeviceCheckInTask checkInSequence = checkInTaskProvider.get();
        return checkInSequence.run();
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
