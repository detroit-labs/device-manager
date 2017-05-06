package com.detroitlabs.devicemanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.detroitlabs.devicemanager.databinding.ViewDeviceDetailBinding;
import com.detroitlabs.devicemanager.models.Device;

public class DeviceDetailView extends FrameLayout {
    private ViewDeviceDetailBinding binding;

    public DeviceDetailView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        binding = ViewDeviceDetailBinding.inflate(LayoutInflater.from(context), this, true);
    }

    public void setDetail(Device device) {
        binding.setDetail(device);
        binding.executePendingBindings();
    }
}
