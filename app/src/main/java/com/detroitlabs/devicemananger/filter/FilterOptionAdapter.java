package com.detroitlabs.devicemananger.filter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.detroitlabs.devicemananger.HighlightableTextView;
import com.detroitlabs.devicemananger.R;

import java.util.ArrayList;
import java.util.List;

class FilterOptionAdapter extends RecyclerView.Adapter<FilterOptionAdapter.OptionViewHolder> {

    private List<String> optionList;

    FilterOptionAdapter() {
        optionList = new ArrayList<>();
    }

    class OptionViewHolder extends RecyclerView.ViewHolder {
        private HighlightableTextView optionItem;

        OptionViewHolder(View itemView) {
            super(itemView);
            this.optionItem = (HighlightableTextView) itemView;
        }
        void bind(String text) {
            optionItem.setText(text);
        }
    }


    @Override
    public OptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_option_item, parent, false);
        return new OptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OptionViewHolder holder, int position) {
        if (!optionList.isEmpty()) {
            holder.bind(optionList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return optionList.size();
    }

    void setData(List<String> optionList) {
        this.optionList = optionList;
        notifyDataSetChanged();
    }
}