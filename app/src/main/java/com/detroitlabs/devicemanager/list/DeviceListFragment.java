package com.detroitlabs.devicemanager.list;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import com.detroitlabs.devicemanager.data.DatabaseContract;
import com.detroitlabs.devicemanager.databinding.FragDeviceListBinding;
import com.detroitlabs.devicemanager.detail.DeviceDetailView;
import com.detroitlabs.devicemanager.filter.FilterUtil;
import com.detroitlabs.devicemanager.models.Device;


public class DeviceListFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        OnItemClickListener {

    public static final int LOADER_ID = 113;
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
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(),
                DatabaseContract.DEVICE_URI,
                null,
                FilterUtil.getDeviceListQuery(),
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        adapter.swapCursor(null);
    }

    @Override
    public void onItemClick(Device device) {
        binding.deviceDetail.setDetail(device);
        openDrawer();
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
        getLoaderManager().restartLoader(LOADER_ID, null, this);
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