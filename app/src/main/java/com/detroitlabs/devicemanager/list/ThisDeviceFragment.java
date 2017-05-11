package com.detroitlabs.devicemanager.list;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.detroitlabs.devicemanager.R;
import com.detroitlabs.devicemanager.databinding.ViewDeviceListItemBinding;
import com.detroitlabs.devicemanager.models.Device;

import static com.detroitlabs.devicemanager.constants.Constants.BROADCAST_ACTION_REQUEST_RECEIVED;
import static com.detroitlabs.devicemanager.constants.Constants.BROADCAST_EXTRA_REQUESTED_BY;

public class ThisDeviceFragment extends Fragment implements LoaderManager.LoaderCallbacks<Device> {
    private static final String TAG = ThisDeviceFragment.class.getSimpleName();
    private static final int LOADER_ID = 333;
    private ViewDeviceListItemBinding binding;
    private OnItemClickListener listener;
    private Device device;
    private BroadcastReceiver requestReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BROADCAST_ACTION_REQUEST_RECEIVED)) {
                String requestedBy = intent.getStringExtra(BROADCAST_EXTRA_REQUESTED_BY);
                showDialog(requestedBy);
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ViewDeviceListItemBinding.inflate(inflater, container, false);
        initView();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_ACTION_REQUEST_RECEIVED);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(requestReceiver, intentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(requestReceiver);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private void initView() {
        binding.status.setImageResource(R.drawable.ic_smartphone_white_24dp);
        binding.status.setColorFilter(getColor(R.color.grey));
        binding.deviceItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null && device != null) {
                    listener.onItemClick(device);
                }
            }
        });
    }

    private void setupData(Device device) {
        this.device = device;
        binding.setDetail(device);
        setStatus(device.isCheckedOut());
        binding.executePendingBindings();
    }

    private void setStatus(boolean isCheckedOut) {
        if (!isCheckedOut) {
            binding.status.setColorFilter(getColor(R.color.green));
        } else {
            binding.status.setColorFilter(getColor(R.color.grey));
        }
    }

    private int getColor(@ColorRes int colorRes) {
        return ContextCompat.getColor(getContext(), colorRes);
    }

    private void showDialog(String requestedBy) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.request_from, requestedBy));
        builder.setTitle(R.string.device_request);
        builder.setPositiveButton(android.R.string.ok, null);
        builder.create().show();
    }

    @Override
    public Loader<Device> onCreateLoader(int id, Bundle args) {
        return new ThisDeviceTaskLoader(getContext());
    }

    @Override
    public void onLoadFinished(Loader<Device> loader, Device data) {
        setupData(data);
    }

    @Override
    public void onLoaderReset(Loader<Device> loader) {

    }
}
