package com.detroitlabs.devicemanager.ui;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.detroitlabs.devicemanager.databinding.ViewDeviceListItemBinding;
import com.detroitlabs.devicemanager.db.Device;

import java.util.List;


public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.DeviceHolder> {

    private OnItemClickListener listener;
    private List<Device> devices;

    public DeviceListAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public DeviceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDeviceListItemBinding binding = ViewDeviceListItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        binding.setListener(listener);
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
