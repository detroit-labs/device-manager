package com.detroitlabs.devicemananger;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.detroitlabs.devicemananger.databinding.ActivityMainBinding;
import com.detroitlabs.devicemananger.filter.SearchFilterDialog;
import com.detroitlabs.devicemananger.list.DeviceListFragment;
import com.detroitlabs.devicemananger.models.Device;
import com.detroitlabs.devicemananger.utils.DeviceUtil;

public class MainActivity extends AppCompatActivity implements DeviceListFragment.OnItemClickListener {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setupToolbar();
        setupRightDrawer();
        setupDeviceListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.info) {
            Device detail = DeviceUtil.getDeviceDetail(this);
            binding.deviceDetail.setDetail(detail);
            openDrawer();
        } else if (id == R.id.filter) {
            showSearchFilterDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSearchFilterDialog() {
        FragmentManager fm = getSupportFragmentManager();
        SearchFilterDialog searchFilterView = SearchFilterDialog.newInstance();
        searchFilterView.show(fm, "search_filter_dialog");
    }

    private void setupToolbar() {
        setSupportActionBar(binding.included1.toolbar);
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
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
    }

    private void setupDeviceListView() {
        DeviceListFragment deviceListFragment = new DeviceListFragment();
        deviceListFragment.setOnItemClickListener(this);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, deviceListFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(Device device) {
        binding.deviceDetail.setDetail(device);
        openDrawer();
    }

    private void openDrawer() {
        binding.drawerLayout.openDrawer(Gravity.END);
    }
}
