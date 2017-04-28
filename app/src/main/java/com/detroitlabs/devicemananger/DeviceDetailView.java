package com.detroitlabs.devicemananger;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.detroitlabs.devicemananger.databinding.ViewDeviceDetailBinding;
import com.detroitlabs.devicemananger.models.Device;

public class DeviceDetailView extends FrameLayout {
    private ViewDeviceDetailBinding binding;

    public DeviceDetailView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        binding = ViewDeviceDetailBinding.inflate(LayoutInflater.from(context), this, true);
        binding.version.setValue("7.1.2");
    }

    public void setDetail(Device device) {
        binding.setDetail(device);
        binding.executePendingBindings();
    }
}
