package com.detroitlabs.devicemanager.detail;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.detroitlabs.devicemanager.R;
import com.detroitlabs.devicemanager.data.DeviceUpdateService;
import com.detroitlabs.devicemanager.databinding.ViewDeviceDetailBinding;
import com.detroitlabs.devicemanager.models.Device;

public class DeviceDetailView extends FrameLayout {
    private BackButtonClickListener listener;

    public interface BackButtonClickListener {

        void onClick();
    }

    private ViewDeviceDetailBinding binding;

    public DeviceDetailView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        binding = ViewDeviceDetailBinding.inflate(LayoutInflater.from(context), this, true);
    }

    // TODO: 5/10/17 handle rotation when this view is displayed
    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }

    public void setDetail(Device device) {
        binding.setDetail(device);
        setTitle(device);
        setStatus(device);
        setButtons(device);
        binding.executePendingBindings();

    }

    public void onClosed() {
        binding.requestName.getEditableText().clear();
    }

    private void setButtons(final Device device) {
        binding.requestName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.requestButton.setEnabled(s.length() > 0);
            }
        });
        binding.requestButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceUpdateService.requestDevice(
                        getContext(),
                        device.serialNumber,
                        binding.requestName.getText().toString()
                );
                navigateUp();
            }
        });

        if (device.isCheckedOut()) {
            showButton(binding.requestField);
        } else {
            hideAllButtons();
        }


    }

    private void navigateUp() {
        if (listener != null) {
            listener.onClick();
        }
    }

    private void showButton(View view) {
        hideAllButtons();
        view.setVisibility(VISIBLE);
    }

    private void hideAllButtons() {
        binding.requestField.setVisibility(GONE);
    }

    private void setTitle(Device device) {
        String brandAndModel = device.brandAndModel;
        binding.collapsingToolbar.setTitle(brandAndModel);
    }

    private void setStatus(Device device) {
        if (device.isCheckedOut()) {
            binding.status.setText(getString(R.string.checked_out_by, device.checkedOutBy));
        } else {
            binding.status.setText(R.string.available);
        }
    }

    public void setBackButtonClickListener(final BackButtonClickListener listener) {
        this.listener = listener;
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
