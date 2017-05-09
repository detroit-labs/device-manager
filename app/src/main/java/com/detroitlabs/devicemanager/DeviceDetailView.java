package com.detroitlabs.devicemanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.detroitlabs.devicemanager.databinding.ViewDeviceDetailBinding;
import com.detroitlabs.devicemanager.models.Device;

public class DeviceDetailView extends FrameLayout {
    public interface BackButtonClickListener {
        void onClick();
    }

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
        setStatus(device);
        binding.executePendingBindings();

    }

    private void setStatus(Device device) {
        if (device.isCheckedOut()) {
            binding.status.setText(getString(R.string.borrowed_by, device.checkedOutBy));
            binding.requestButton.setEnabled(true);
            binding.requestButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else {
            binding.status.setText(R.string.available);
            binding.requestButton.setEnabled(false);
        }
    }

    public void setBackButtonClickListener(final BackButtonClickListener listener) {
        binding.toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick();
                }
            }
        });
    }

    private String getString(@StringRes int stringRes, Object... args) {
        return getContext().getString(stringRes, args);
    }
}
