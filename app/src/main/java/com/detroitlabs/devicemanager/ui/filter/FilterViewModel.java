package com.detroitlabs.devicemanager.ui.filter;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.detroitlabs.devicemanager.DmApplication;
import com.detroitlabs.devicemanager.constants.FilterType;
import com.detroitlabs.devicemanager.repository.DeviceRepository;

import java.util.Set;

import javax.inject.Inject;


public class FilterViewModel extends ViewModel {
    private final LiveData<Filter.Options> allOptions;
    private final LiveData<Filter.Options> filteredOptions;
    private final Filter.Selection selections;

    @Inject
    DeviceRepository deviceRepo;

    public FilterViewModel() {
        DmApplication.getInjector().inject(this);
        allOptions = deviceRepo.loadAllFilterOptions();
        selections = new Filter.Selection();
        filteredOptions = deviceRepo.loadAllFilterOptions(selections);
    }

    LiveData<Filter.Options> loadAllOptions() {
        return allOptions;
    }

    void updateFilter(FilterType filterType, String value, boolean isActivated) {
        if (isActivated) {
            selections.addSelection(filterType, value);
        } else {
            selections.removeSelection(filterType, value);
        }
        deviceRepo.refreshList(selections);
    }

    LiveData<Filter.Options> loadFilteredOptions() {
        return filteredOptions;
    }

    void clearAllSelections() {
        selections.removeAllSelections();
        deviceRepo.refreshList(selections);
    }

    Set<String> getSelections(FilterType filterType) {
        return selections.getSelectionValues(filterType);
    }
}
