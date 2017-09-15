package com.detroitlabs.devicemanager.ui.binding;


import android.databinding.BindingAdapter;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.detroitlabs.devicemanager.R;
import com.detroitlabs.devicemanager.db.Device;

public class BindingAdapters {
    @BindingAdapter("visibleGone")
    public static void showHide(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("color")
    public static void tintIcon(ImageView imageView, Device device) {
        if (device.hasRequest()) {
            imageView.setColorFilter(ContextCompat.getColor(imageView.getContext(), R.color.orange));
        } else if (device.isCheckedOut()) {
            imageView.setColorFilter(ContextCompat.getColor(imageView.getContext(), R.color.red));
        } else {
            imageView.setColorFilter(ContextCompat.getColor(imageView.getContext(), R.color.green));
        }
    }

    @BindingAdapter("status_text")
    public static void setStatusText(TextView textView, Device device) {
        if (device.hasRequest()) {
            textView.setText(textView.getContext().getString(R.string.requested_by, device.requestedBy));
        } else if (device.isCheckedOut()) {
            textView.setText(textView.getContext().getString(R.string.checked_out_by, device.checkedOutBy));
        } else {
            textView.setText(R.string.available);
        }
    }
}
