package com.detroitlabs.devicemanager.ui;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.detroitlabs.devicemanager.R;
import com.detroitlabs.devicemanager.databinding.FragDeviceListBinding;
import com.detroitlabs.devicemanager.detail.DeviceDetailView;

import java.util.List;


public class DeviceListFragment extends LifecycleFragment implements
        OnItemClickListener {

    private static final String HOME_FRAGMENT = "HomeFragment";

    private FragDeviceListBinding binding;
    private DeviceListAdapter adapter;


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
        DeviceListViewModel viewModel = ViewModelProviders.of(this).get(DeviceListViewModel.class);
        viewModel.getDeviceList().observe(this, new Observer<List<com.detroitlabs.devicemanager.db.Device>>() {
            @Override
            public void onChanged(@Nullable List<com.detroitlabs.devicemanager.db.Device> devices) {
                adapter.setData(devices);
            }
        });
    }

    @Override
    public void onItemClick(com.detroitlabs.devicemanager.db.Device device) {
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

    public void refreshList() {

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's back button
            case android.R.id.home:
                HomeFragment homeFragment = HomeFragment.newInstance();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, homeFragment, HOME_FRAGMENT);
                fragmentTransaction.commit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
