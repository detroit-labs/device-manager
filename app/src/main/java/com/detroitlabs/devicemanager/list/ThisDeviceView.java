package com.detroitlabs.devicemanager.list;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.detroitlabs.devicemanager.R;
import com.detroitlabs.devicemanager.databinding.ViewDeviceListItemBinding;

import static com.detroitlabs.devicemanager.utils.DeviceUtil.THIS_DEVICE;

public class ThisDeviceView extends FrameLayout{

    private ViewDeviceListItemBinding binding;

    public ThisDeviceView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        binding = ViewDeviceListItemBinding.inflate(LayoutInflater.from(context),
                this,
                true);
        binding.status.setImageResource(R.drawable.ic_smartphone_white_24dp);
        binding.status.setColorFilter(ContextCompat.getColor(getContext(),R.color.grey));
        binding.setDetail(THIS_DEVICE);
        binding.deviceItemView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.light_indigo));
    }


}
