package com.detroitlabs.devicemanager;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;


public class HighlightableTextView extends AppCompatTextView {

    public interface OnHighlightListener {

        void onHighlight(boolean isHighlighted);
    }

    private boolean isHighlighted;

    private OnHighlightListener onHighlightListener;

    public HighlightableTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        Resources resources = getResources();
        @Px int padding = resources.getDimensionPixelOffset(R.dimen.small_padding);
        @Px int minWidth = resources.getDimensionPixelOffset(R.dimen.highlightable_text_min_width);
        setPadding(padding, padding, padding, padding);
        setMinWidth(minWidth);
        setGravity(Gravity.CENTER);
        setBackgroundResource(R.drawable.highlightable_background);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HighlightableTextView.super.isEnabled()) {
                    isHighlighted = !isHighlighted;
                    v.setActivated(isHighlighted);
                    if (onHighlightListener != null) {
                        onHighlightListener.onHighlight(isHighlighted);
                    }
                }
            }
        });
    }

    public void setHighlighted(boolean isHighlighted) {
        this.isHighlighted = isHighlighted;
        setActivated(isHighlighted);
    }

    public void setOnHighlightListener(OnHighlightListener onHighlightListener) {
        this.onHighlightListener = onHighlightListener;
    }
}
