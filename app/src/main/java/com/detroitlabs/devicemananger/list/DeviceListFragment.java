package com.detroitlabs.devicemananger.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.detroitlabs.devicemananger.databinding.FragDeviceListBinding;
import com.detroitlabs.devicemananger.filter.FilterUtil;
import com.detroitlabs.devicemananger.models.Device;

import java.util.Collections;
import java.util.List;


public class DeviceListFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<List<Device>>,
        DeviceListAdapter.OnItemClickListener {
    private FragDeviceListBinding binding;
    private DeviceListAdapter adapter;
    private OnItemClickListener listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragDeviceListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecyclerView();
        getLoaderManager().initLoader(DeviceListTaskLoader.LOADER_ID, null, this);
    }

    private void initRecyclerView() {
        binding.deviceList.setHasFixedSize(true);
        adapter = new DeviceListAdapter(this);
        binding.deviceList.setAdapter(adapter);
        binding.deviceList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public Loader<List<Device>> onCreateLoader(int id, Bundle args) {
        return new DeviceListTaskLoader(getContext());
    }

    @Override
    public void onLoadFinished(Loader<List<Device>> loader, List<Device> data) {
        // apply filter to data
        adapter.setData(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        adapter.setData(Collections.<Device>emptyList());
    }

    @Override
    public void onItemClick(Device device) {
        if (listener != null) {
            listener.onClick(device);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onClick(Device device);
    }
}
