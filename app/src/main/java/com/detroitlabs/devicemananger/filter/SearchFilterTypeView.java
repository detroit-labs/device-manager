package com.detroitlabs.devicemananger.filter;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.detroitlabs.devicemananger.R;
import com.detroitlabs.devicemananger.databinding.ViewFilterTypeBinding;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class SearchFilterTypeView extends FrameLayout {
    private final String title;
    private ViewFilterTypeBinding binding;

    public SearchFilterTypeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SearchFilterTypeView, 0, 0);
        try {
            title = a.getString(R.styleable.SearchFilterTypeView_title);
        } finally {
            a.recycle();
        }
        initView(context);
    }

    private void initView(Context context) {
        binding = ViewFilterTypeBinding.inflate(LayoutInflater.from(context), this, true);
        binding.title.setText(title);
        initRecyclerView();
    }

    private void initRecyclerView() {
        FilterOptionAdapter adapter = new FilterOptionAdapter();
        binding.options.setAdapter(adapter);
        binding.options.setHasFixedSize(true);
        FlowLayoutManager layoutManager = new FlowLayoutManager();
        layoutManager.setAutoMeasureEnabled(true);
        binding.options.setLayoutManager(layoutManager);
        binding.options.addItemDecoration(new SpacesItemDecoration(16));
        adapter.setData(getTestData());

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
}
