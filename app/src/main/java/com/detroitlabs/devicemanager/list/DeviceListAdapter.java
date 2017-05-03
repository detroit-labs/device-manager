package com.detroitlabs.devicemanager.list;

import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.detroitlabs.devicemanager.R;
import com.detroitlabs.devicemanager.databinding.ViewDeviceListItemBinding;
import com.detroitlabs.devicemanager.models.Device;


public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.DeviceHolder> {

    private OnItemClickListener listener;
    private Cursor cursor;

    public DeviceListAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public DeviceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDeviceListItemBinding binding = ViewDeviceListItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new DeviceHolder(binding);
    }

    @Override
    public void onBindViewHolder(DeviceHolder holder, int position) {
        if (!cursor.moveToPosition(position)) {
            throw new IllegalStateException("Invalid item position requested");
        }
        holder.bind(new Device(cursor));
    }

    @Override
    public int getItemCount() {
        return (cursor != null) ? cursor.getCount() : 0;
    }

    public void swapCursor(Cursor cursor) {
        if (this.cursor != null) {
            this.cursor.close();
        }
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    class DeviceHolder extends RecyclerView.ViewHolder {

        private final ViewDeviceListItemBinding binding;

        DeviceHolder(ViewDeviceListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(final Device detail) {
            binding.setDetail(detail);
            setStatus(detail.isCheckedOut());
            binding.executePendingBindings();
            binding.deviceItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(detail);
                    }
                }
            });
        }

        private void setStatus(boolean isCheckedOut) {
            if (!isCheckedOut) {
                binding.status.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.green));
            } else {
                binding.status.clearColorFilter();
            }
        }
    }


    interface OnItemClickListener {
        void onItemClick(Device device);
    }
}
