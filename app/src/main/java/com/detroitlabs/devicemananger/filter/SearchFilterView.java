package com.detroitlabs.devicemananger.filter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.detroitlabs.devicemananger.databinding.ViewSearchFilterBinding;

public class SearchFilterView extends FrameLayout {
    private ViewSearchFilterBinding binding;

    public SearchFilterView(@NonNull Context context) {
        this(context, null);
    }
    public SearchFilterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        binding = ViewSearchFilterBinding.inflate(LayoutInflater.from(context), this, true);

    }
}
