package com.detroitlabs.devicemanager.ui;

import android.os.Looper;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.detroitlabs.devicemanager.databinding.ViewDeviceListItemBinding;
import com.detroitlabs.devicemanager.db.Device;
import com.detroitlabs.devicemanager.utils.ViewUtil;

import java.util.List;


public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.DeviceHolder> {

    private List<Device> devices;

    @Override
    public DeviceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDeviceListItemBinding binding = ViewDeviceListItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        binding.setItemClickListener(getItemClickListener(binding));
        binding.setRequestButtonClickListener(getRequestButtonClickListener(binding));
        binding.enterName.setOnFocusChangeListener(getOnRequestFocusChangedListener());
        return new DeviceHolder(binding);
    }

    @Override
    public void onBindViewHolder(DeviceHolder holder, int position) {
        Device device = devices.get(position);
        holder.bind(device);
    }

    @Override
    public int getItemCount() {
        return devices == null ? 0 : devices.size();
    }

    public void swapItems(List<Device> devices) {
        if (this.devices == null) {
            setData(devices);
            return;
        }

        final DeviceDiffCallback diffCallback = new DeviceDiffCallback(this.devices, devices);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.devices.clear();
        this.devices.addAll(devices);

        diffResult.dispatchUpdatesTo(this);
    }

    private void setData(List<Device> devices) {
        this.devices = devices;
        notifyDataSetChanged();
    }

    private OnItemClickListener getItemClickListener(final ViewDeviceListItemBinding binding) {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(Device device) {
                if (binding.enterNameArea.getVisibility() == View.VISIBLE) {
                    binding.enterName.getText().clear();
                    ViewUtil.toggleViewAnimated(binding.requestArea, binding.requestButton, binding.enterNameArea);
                }
                ViewUtil.toggleView(binding.detail);
            }
        };
    }

    private View.OnFocusChangeListener getOnRequestFocusChangedListener() {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    ViewUtil.hideKeyboard(view);
                }
            }
        };
    }

    private OnItemClickListener getRequestButtonClickListener(final ViewDeviceListItemBinding binding) {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(Device device) {
                ViewUtil.toggleViewAnimated(binding.requestArea, binding.requestButton, binding.enterNameArea);
            }
        };
    }

    class DeviceHolder extends RecyclerView.ViewHolder {

        private final ViewDeviceListItemBinding binding;

        DeviceHolder(ViewDeviceListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(final Device device) {
            binding.setDevice(device);
            binding.executePendingBindings();
        }
    }
}
