package com.detroitlabs.devicemanager.ui.filter;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.detroitlabs.devicemanager.R;
import com.detroitlabs.devicemanager.constants.FilterType;
import com.detroitlabs.devicemanager.databinding.ViewSearchFilterBinding;
import com.detroitlabs.devicemanager.ui.filter.adapters.FilterOptionAdapter;

import java.util.Set;


public class SearchFilterDialog extends DialogFragment implements
        FilterOptionAdapter.OnFilterUpdatedListener, LifecycleRegistryOwner {

    private static final String TAG = SearchFilterDialog.class.getName();
    private ViewSearchFilterBinding binding;
    private FilterOptionAdapter[] adapters;
    private FilterViewModel viewModel;
    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);


    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    public static SearchFilterDialog newInstance() {
        Bundle args = new Bundle();
        SearchFilterDialog fragment = new SearchFilterDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ViewSearchFilterBinding.inflate(inflater, container, false);
        initFilterUi();
        return binding.getRoot();
    }

    public void setViewModel(FilterViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        binding.titleBar.titleText.setText(R.string.search_filter);
        binding.buttonBar.okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        binding.titleBar.topButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.clearAllSelections();
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fetchFilters();
    }

    private void initFilterUi() {
        adapters = FilterUtil.createAdapters();
        for (FilterOptionAdapter adapter : adapters) {
            adapter.setOnFilterUpdatedListener(this);
            SearchFilterTypeView filterTypeView =
                    new SearchFilterTypeView(getContext())
                            .setAdapter(adapter);
            binding.filterContainer.addView(filterTypeView);
        }
    }

    private void fetchFilters() {
        viewModel.loadAllOptions().observe(this, new Observer<Filter.Options>() {
            @Override
            public void onChanged(@Nullable Filter.Options options) {
                for (FilterOptionAdapter adapter : adapters) {
                    Set<String> values = options.getOptionValues(adapter.getFilterType());
                    adapter.setAllOptions(values);
                }
            }
        });
        viewModel.loadFilteredOptions().observe(this, new Observer<Filter.Options>() {
            @Override
            public void onChanged(@Nullable Filter.Options options) {
                for (FilterOptionAdapter adapter : adapters) {
                    Set<String> newOptions = options.getOptionValues(adapter.getFilterType());
                    Set<String> selections = viewModel.getSelections(adapter.getFilterType());
                    adapter.setOptions(newOptions, selections);
                }
            }
        });
    }

    @Override
    public void onFilterUpdated(FilterType filterType, String value, boolean isActivated) {
        viewModel.updateFilter(filterType, value, isActivated);
    }
}
