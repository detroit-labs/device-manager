package com.detroitlabs.devicemanager.filter;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.detroitlabs.devicemanager.databinding.ViewFilterTypeBinding;
import com.detroitlabs.devicemanager.filter.adapters.FilterOptionAdapter;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class SearchFilterTypeView extends FrameLayout {
    private ViewFilterTypeBinding binding;
    private FilterOptionAdapter adapter;

    public SearchFilterTypeView(@NonNull Context context) {
        super(context);
    }

    public SearchFilterTypeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        initView(getContext());
    }

    public SearchFilterTypeView setAdapter(FilterOptionAdapter adapter) {
        this.adapter = adapter;
        return this;
    }

    private void initView(Context context) {
        binding = ViewFilterTypeBinding.inflate(LayoutInflater.from(context), this, true);
        binding.title.setText(adapter.getTitleRes());
        initRecyclerView();
    }

    private void initRecyclerView() {
        binding.options.setAdapter(adapter);
        binding.options.setHasFixedSize(true);
        FlowLayoutManager layoutManager = new FlowLayoutManager();
        layoutManager.setAutoMeasureEnabled(true);
        binding.options.setLayoutManager(layoutManager);
        binding.options.addItemDecoration(new SpacesItemDecoration(16));
    }

    // TODO: 4/27/17 remove this
    private List<String> getTestData() {
        List<String> testData = new ArrayList<>();
        testData.add("5.0");
        testData.add("5.1");
        testData.add("6.0");
        testData.add("6.1");
        testData.add("7.0");
        testData.add("7.1.2");
        return testData;
    }

    private class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private final int mSpace;

        SpacesItemDecoration(int space) {
            this.mSpace = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = mSpace;
            outRect.right = mSpace;
            outRect.bottom = mSpace;
        }
    }
}
