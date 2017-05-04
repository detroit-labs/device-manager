package com.detroitlabs.devicemanager.filter.adapters;

import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.detroitlabs.devicemanager.HighlightableTextView;
import com.detroitlabs.devicemanager.R;
import com.detroitlabs.devicemanager.constants.FilterType;
import com.detroitlabs.devicemanager.filter.FilterUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class FilterOptionAdapter extends RecyclerView.Adapter<FilterOptionAdapter.OptionViewHolder> {

    private OnFilterUpdatedListener listener;
    private List<String> items;
    private Set<String> newOptions;

    public abstract FilterType getFilterType();

    @StringRes
    public abstract int getTitleRes();

    public interface OnFilterUpdatedListener {
        void onFilterUpdated();
    }

    @Override
    public OptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_option_item, parent, false);
        return new OptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OptionViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        if (items == null || items.isEmpty()) {
            return 0;
        } else {
            return items.size();
        }
    }

    public void setOnFilterUpdatedListener(OnFilterUpdatedListener listener) {
        this.listener = listener;
    }

    public void setOptions(Set<String> allOptions, Set<String> options) {
        if (items == null) {
            items = new ArrayList<>(allOptions);
        }
        this.newOptions = options;
        notifyDataSetChanged();
    }

    class OptionViewHolder extends RecyclerView.ViewHolder {
        private HighlightableTextView optionItem;

        OptionViewHolder(View itemView) {
            super(itemView);
            this.optionItem = (HighlightableTextView) itemView;
        }

        void bind(final String text) {
            optionItem.setText(text);
            if (newOptions != null) {
                optionItem.setEnabled(newOptions.contains(text));
            }
            optionItem.setHighlighted(FilterUtil.containsSelection(getFilterType(), text));
            optionItem.setOnHighlightListener(new HighlightableTextView.OnHighlightListener() {
                @Override
                public void onHighlight(boolean isHighlighted) {
                    if (isHighlighted) {
                        FilterUtil.addSelection(getFilterType(), text);
                    } else {
                        FilterUtil.removeSelection(getFilterType(), text);
                    }
                    if (listener != null) {
                        listener.onFilterUpdated();
                    }
                }
            });
        }
    }
}