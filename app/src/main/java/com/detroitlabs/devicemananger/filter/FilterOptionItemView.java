package com.detroitlabs.devicemananger.filter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.detroitlabs.devicemananger.HighlightableTextView;

public class FilterOptionItemView extends HighlightableTextView {
    public FilterOptionItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {

    }
}
