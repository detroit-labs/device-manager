package com.detroitlabs.devicemanager.filter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.detroitlabs.devicemanager.R;
import com.detroitlabs.devicemanager.databinding.ViewSearchFilterBinding;
import com.detroitlabs.devicemanager.filter.adapters.FilterOptionAdapter;
import com.detroitlabs.devicemanager.models.Filter;

import java.util.Collections;
import java.util.Set;


public class SearchFilterDialog extends DialogFragment implements
        LoaderManager.LoaderCallbacks<Filter.Options>,
        FilterOptionAdapter.OnFilterUpdatedListener {

    private static final int LOADER_ID = 233;
    private ViewSearchFilterBinding binding;
    private FilterOptionAdapter[] adapters;
    private OnFilterApplyListener onApplyListener;

    public interface OnFilterApplyListener {
        void onApply();
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
        initFilter();
        return binding.getRoot();
    }

    private void initFilter() {
        initFilterTypes();
        initFilterUi();
    }

    private void initFilterTypes() {
        // fetch data from firebase db and load the Filter
    }

    private void initFilterUi() {
        adapters = FilterUtil.createAdapters();
        for (FilterOptionAdapter adapter : adapters) {
            adapter.setOnFilterUpdatedListener(this);
            SearchFilterTypeView filterTypeView =
                    new SearchFilterTypeView(getActivity())
                            .setAdapter(adapter);
            binding.filterContainer.addView(filterTypeView);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        binding.titleBar.titleText.setText(R.string.search_filter);
        binding.buttonBar.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        binding.buttonBar.applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onApplyListener != null) {
                    onApplyListener.onApply();
                }
                dismiss();
            }
        });
        fetchFilters();
    }

    private void fetchFilters() {
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Filter.Options> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID) {
            return new FilterTaskLoader(getActivity());
        }
        throw new IllegalArgumentException("Illegal loader id");
    }

    @Override
    public void onLoadFinished(Loader<Filter.Options> loader, Filter.Options data) {
        if (FilterUtil.firstTimeOpened()) {
            FilterUtil.setAllOptions(data);
        }
        for (FilterOptionAdapter adapter : adapters) {
            Set<String> allOptions = FilterUtil.getAllOptionValues(adapter.getFilterType());
            Set<String> options = data.getOptionValues(adapter.getFilterType());
            adapter.setOptions(allOptions, options);
        }
    }

    @Override
    public void onLoaderReset(Loader<Filter.Options> loader) {
        for (FilterOptionAdapter adapter : adapters) {
            adapter.setOptions(Collections.<String>emptySet(),
                    Collections.<String>emptySet());
        }
    }

    /* Implementation FilterOptionAdapter.OnFilterUpdatedListener */
    @Override
    public void onFilterUpdated() {
        // TODO: 5/1/17 add spinner to throttle clicking
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    public void setOnApplyListener(OnFilterApplyListener onApplyListener) {
        this.onApplyListener = onApplyListener;
    }
}
