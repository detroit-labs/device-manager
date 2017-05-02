package com.detroitlabs.devicemanager.list;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.detroitlabs.devicemanager.R;
import com.detroitlabs.devicemanager.databinding.ViewDetailItemBinding;


public class DetailItemView extends FrameLayout {
    private final ViewDetailItemBinding binding;

    public DetailItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        binding = ViewDetailItemBinding.inflate(LayoutInflater.from(context), this, true);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DetailItemView, 0, 0);
        try {
            String label = a.getString(R.styleable.DetailItemView_label);
            String value = a.getString(R.styleable.DetailItemView_value);
            setLabel(label);
            setValue(value);
        } finally {
            a.recycle();
        }
    }

    public void setLabel(String label) {
        binding.label.setText(label);
    }

    public void setValue(String value) {
        binding.value.setText(value);
    }
}
