package com.detroitlabs.devicemanager.ui.filter.adapters;

import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.detroitlabs.devicemanager.HighlightableTextView;
import com.detroitlabs.devicemanager.R;
import com.detroitlabs.devicemanager.constants.FilterType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class FilterOptionAdapter extends RecyclerView.Adapter<FilterOptionAdapter.OptionViewHolder> {

    private OnFilterUpdatedListener listener;
    private List<String> allOptions;
    private Set<String> newOptions;
    private Set<String> selections = new HashSet<>();

    public abstract FilterType getFilterType();

    @StringRes
    public abstract int getTitleRes();

    public interface OnFilterUpdatedListener {

        void onFilterUpdated(FilterType filterType, String value, boolean isActivated);
    }
    @Override
    public OptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_option_item, parent, false);
        return new OptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OptionViewHolder holder, int position) {
        holder.bind(allOptions.get(position));
    }

    @Override
    public int getItemCount() {
        return allOptions == null ? 0 : allOptions.size();
    }

    public void setOnFilterUpdatedListener(OnFilterUpdatedListener listener) {
        this.listener = listener;
    }

    public void setOptions(Set<String> options, Set<String> selections) {
        this.newOptions = options;
        if (!selections.isEmpty()) {
            this.selections = selections;
        } else {
            this.selections.clear();
        }
        notifyDataSetChanged();
    }

    public void setAllOptions(Set<String> allOptions) {
        this.allOptions = new ArrayList<>(allOptions);
        notifyDataSetChanged();
    }

    class OptionViewHolder extends RecyclerView.ViewHolder {
        private HighlightableTextView optionItem;

        OptionViewHolder(View itemView) {
            super(itemView);
            this.optionItem = (HighlightableTextView) itemView;
        }

        void bind(String text) {
            optionItem.setText(text);
            if (newOptions != null && !newOptions.isEmpty()) {
                optionItem.setEnabled(newOptions.contains(text));
            }
            optionItem.setHighlighted(selections.contains(text));
            optionItem.setOnHighlightListener(new HighlightableTextView.OnHighlightListener() {
                @Override
                public void onHighlight(boolean isHighlighted, CharSequence text) {
                    if (isHighlighted) {
                        selections.add(text.toString());
                    } else {
                        selections.remove(text.toString());
                    }
                    listener.onFilterUpdated(getFilterType(), text.toString(), isHighlighted);
                }
            });
        }
    }
}