package com.detroitlabs.devicemanager.ui.detail;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.detroitlabs.devicemanager.R;


public class DetailItemView extends FrameLayout {

    private final TextView label;
    private final TextView value;

    public DetailItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_detail_item, this);
        label = view.findViewById(R.id.label);
        value = view.findViewById(R.id.value);
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

    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }

    public void setLabel(String label) {
        this.label.setText(label);
    }

    public void setValue(String value) {
        this.value.setText(value);
    }
}
