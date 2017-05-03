package com.detroitlabs.devicemanager.list;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.detroitlabs.devicemanager.data.DatabaseContract;
import com.detroitlabs.devicemanager.databinding.FragDeviceListBinding;
import com.detroitlabs.devicemanager.filter.FilterUtil;
import com.detroitlabs.devicemanager.models.Device;

import java.util.Collections;


public class DeviceListFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        DeviceListAdapter.OnItemClickListener {

    public static final int LOADER_ID = 113;

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
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    private void initRecyclerView() {
        binding.deviceList.setHasFixedSize(true);
        adapter = new DeviceListAdapter(this);
        binding.deviceList.setAdapter(adapter);
        binding.deviceList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(),
                DatabaseContract.DEVICE_URI,
                null,
                FilterUtil.convertFilterToQuerySelection(),
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        adapter.swapCursor(null);
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

    public void refreshList() {
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    public interface OnItemClickListener {
        void onClick(Device device);
    }
}
