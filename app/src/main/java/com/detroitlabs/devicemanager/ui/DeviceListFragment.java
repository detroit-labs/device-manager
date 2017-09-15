package com.detroitlabs.devicemanager.ui;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.detroitlabs.devicemanager.R;
import com.detroitlabs.devicemanager.databinding.FragDeviceListBinding;
import com.detroitlabs.devicemanager.db.Device;
import com.detroitlabs.devicemanager.ui.filter.FilterViewModel;
import com.detroitlabs.devicemanager.ui.filter.SearchFilterDialog;
import com.detroitlabs.devicemanager.utils.DeviceUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


public class DeviceListFragment extends LifecycleFragment {


    private FragDeviceListBinding binding;
    private DeviceListAdapter adapter;
    private DeviceListViewModel viewModel;
    private FilterViewModel filterViewModel;
    private Observer<List<Device>> observer = new Observer<List<Device>>() {
        @Override
        public void onChanged(@Nullable List<Device> devices) {
            sort(devices);
            removeSelf(devices);
            adapter.swapItems(devices);
        }
    };

    public static DeviceListFragment newInstance() {
        Bundle args = new Bundle();
        DeviceListFragment fragment = new DeviceListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragDeviceListBinding.inflate(inflater, container, false);
        initRecyclerView();
        setupFilterButton();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        filterViewModel = ViewModelProviders.of(this).get(FilterViewModel.class);
        viewModel = ViewModelProviders.of(this).get(DeviceListViewModel.class);
        viewModel.getDeviceList().observe(this, observer);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initRecyclerView() {
        binding.deviceList.setHasFixedSize(true);
        adapter = new DeviceListAdapter();
        binding.deviceList.setAdapter(adapter);
        binding.deviceList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.deviceList.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
    }

    private void setupFilterButton() {
        binding.filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSearchFilterDialog();
            }
        });
    }

    private void showSearchFilterDialog() {
        FragmentManager fm = getFragmentManager();
        SearchFilterDialog searchFilterDialog = SearchFilterDialog.newInstance();
        searchFilterDialog.setViewModel(filterViewModel);
        searchFilterDialog.show(fm, "search_filter_dialog");
    }

    private void sort(List<Device> devices) {
        Collections.sort(devices, new Comparator<Device>() {
            @Override
            public int compare(Device d1, Device d2) {
                if (d1.isCheckedOut() == d2.isCheckedOut()) {
                    return d1.brandAndModel.compareTo(d2.brandAndModel);
                } else if (d1.isCheckedOut()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
    }

    private void removeSelf(List<Device> devices) {
        Iterator<Device> iterator = devices.iterator();
        String selfSerialNumber = DeviceUtil.getLocalSerialNumber(getContext());
        while (iterator.hasNext()) {
            if (selfSerialNumber.equalsIgnoreCase(iterator.next().serialNumber)) {
                iterator.remove();
                break;
            }
        }
    }
}
