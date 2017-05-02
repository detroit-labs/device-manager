package com.detroitlabs.devicemananger.filter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.detroitlabs.devicemananger.R;
import com.detroitlabs.devicemananger.databinding.ViewSearchFilterBinding;
import com.detroitlabs.devicemananger.filter.adapters.FilterOptionAdapter;
import com.detroitlabs.devicemananger.models.Filter;

import java.util.List;


public class SearchFilterDialog extends DialogFragment implements
        LoaderManager.LoaderCallbacks<Filter.Options>,
        FilterOptionAdapter.OnFilterUpdatedListener {

    private static final int LOADER_ID = 233;
    private ViewSearchFilterBinding binding;
    private FilterOptionAdapter[] adapters;

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
        adapters = FilterInitializer.createAdapters();
        for (FilterOptionAdapter adapter : adapters) {
            adapter.setOnFilterUpdatedListener(this);
            SearchFilterTypeView filterTypeView =
                    new SearchFilterTypeView(getActivity()).setAdapter(adapter);
            binding.filterContainer.addView(filterTypeView);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        binding.titleBar.titleText.setText(R.string.search_filter);
        fetchFilters();
    }

    private void fetchFilters() {
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }


    @Override
    public Loader<Filter.Options> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID) {
            return new FilterTaskLoader(getActivity(), FilterUtil.getFilterSelection());
        }
        throw new IllegalArgumentException("Illegal loader id");
    }

    @Override
    public void onLoadFinished(Loader<Filter.Options> loader, Filter.Options data) {
        for (FilterOptionAdapter adapter : adapters) {
            List<String> options = data.getOptionValues(adapter.getFilterType());
            adapter.setOptions(options);
        }
    }

    @Override
    public void onLoaderReset(Loader<Filter.Options> loader) {

    }

    /* Implementation FilterOptionAdapter.OnFilterUpdatedListener */
    @Override
    public void onFilterUpdated() {
        // TODO: 5/1/17 add spinner to throttle clicking
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }
}
