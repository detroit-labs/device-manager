package com.detroitlabs.devicemanager.ui;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.detroitlabs.devicemanager.R;
import com.detroitlabs.devicemanager.databinding.FragDeviceListBinding;
import com.detroitlabs.devicemanager.db.Device;
import com.detroitlabs.devicemanager.detail.DeviceDetailView;
import com.detroitlabs.devicemanager.ui.filter.FilterViewModel;
import com.detroitlabs.devicemanager.ui.filter.SearchFilterDialog;

import java.util.List;


public class DeviceListFragment extends LifecycleFragment implements
        OnItemClickListener {

    private static final String HOME_FRAGMENT = "HomeFragment";
    private static final String DEVICE_LIST_FRAGMENT = "DeviceListFragment";

    private FragDeviceListBinding binding;
    private DeviceListAdapter adapter;
    private DeviceListViewModel viewModel;
    private FilterViewModel filterViewModel;
    private Observer<List<Device>> observer = new Observer<List<Device>>() {
        @Override
        public void onChanged(@Nullable List<Device> devices) {
            adapter.setData(devices);
        }
    };

    public static DeviceListFragment newInstance() {
        Bundle args = new Bundle();
        DeviceListFragment fragment = new DeviceListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragDeviceListBinding.inflate(inflater, container, false);
        setupToolbar();
        setupRightDrawer();
        initRecyclerView();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        filterViewModel = ViewModelProviders.of(this).get(FilterViewModel.class);
        viewModel = ViewModelProviders.of(this).get(DeviceListViewModel.class);
        viewModel.getDeviceList().observe(this, observer);
    }

    @Override
    public void onItemClick(Device device) {
//        binding.deviceDetail.setDetail(device);
//        openDrawer();
    }

    public boolean onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(Gravity.END)) {
            binding.drawerLayout.closeDrawer(Gravity.END);
            return true;
        } else {
            return false;
        }
    }

    private void setupRightDrawer() {
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        binding.drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                binding.deviceDetail.onClosed();
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
        binding.deviceDetail.setBackButtonClickListener(new DeviceDetailView.BackButtonClickListener() {
            @Override
            public void onClick() {
                binding.drawerLayout.closeDrawer(Gravity.END);
            }
        });
    }

    private void initRecyclerView() {
        binding.deviceList.setHasFixedSize(true);
        adapter = new DeviceListAdapter(this);
        binding.deviceList.setAdapter(adapter);
        binding.deviceList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void openDrawer() {
        binding.drawerLayout.openDrawer(Gravity.END);
    }

    private void setupToolbar() {
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.included1.toolbar);
        ActionBar bar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's back button
            case android.R.id.home:
                getActivity().getSupportFragmentManager().popBackStack();
                return true;
            case R.id.filter:
                showSearchFilterDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSearchFilterDialog() {
        FragmentManager fm = getFragmentManager();
        SearchFilterDialog searchFilterDialog = SearchFilterDialog.newInstance();
        searchFilterDialog.setViewModel(filterViewModel);
        searchFilterDialog.show(fm, "search_filter_dialog");
    }
}
