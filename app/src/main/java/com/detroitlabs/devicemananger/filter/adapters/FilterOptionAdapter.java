package com.detroitlabs.devicemananger.filter.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.detroitlabs.devicemananger.HighlightableTextView;
import com.detroitlabs.devicemananger.R;
import com.detroitlabs.devicemananger.constants.FilterType;
import com.detroitlabs.devicemananger.filter.FilterUtil;

import java.util.List;
import java.util.Set;

public abstract class FilterOptionAdapter extends RecyclerView.Adapter<FilterOptionAdapter.OptionViewHolder> {

    private OnFilterUpdatedListener listener;
    private List<String> options;

    public abstract FilterType getFilterType();

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
        holder.bind(options.get(position));
    }

    @Override
    public int getItemCount() {
        if (options == null || options.isEmpty()) {
            return 0;
        } else {
            return options.size();
        }
    }

    public void setOnFilterUpdatedListener(OnFilterUpdatedListener listener) {
        this.listener = listener;
    }

    public void setOptions(Set<String> options) {
        // TODO: 5/1/17 disable textview or add animation
    }

    class OptionViewHolder extends RecyclerView.ViewHolder {
        private HighlightableTextView optionItem;

        OptionViewHolder(View itemView) {
            super(itemView);
            this.optionItem = (HighlightableTextView) itemView;
        }

        void bind(final String text) {
            optionItem.setText(text);
            optionItem.setOnHighlightListener(new HighlightableTextView.OnHighlightListener() {
                @Override
                public void onHighlight(boolean isHighlighted) {
                    FilterUtil.updateFilter(getFilterType(), text, isHighlighted);
                    if (listener != null) {
                        listener.onFilterUpdated();
                    }
                }
            });
        }
    }
}