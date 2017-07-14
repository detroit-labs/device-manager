package com.detroitlabs.devicemanager;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;
import com.detroitlabs.devicemanager.filter.SearchFilterDialog;
import com.detroitlabs.devicemanager.ui.DeviceListFragment;
import com.detroitlabs.devicemanager.ui.HomeFragment;
import com.detroitlabs.devicemanager.sync.SyncFragment;
import com.detroitlabs.devicemanager.sync.SyncingService;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements SyncFragment.OnSyncFinishListener {

    private static final String DEVICE_LIST_FRAGMENT = "DeviceListFragment";
    private static final String HOME_FRAGMENT = "HomeFragment";
    private static final String HAS_SYNCED = "HAS_SYNCED";
    private HomeFragment homeFragment;
    private boolean hasSynced;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            hasSynced = savedInstanceState.getBoolean(HAS_SYNCED);
            homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(HOME_FRAGMENT);
        }
        if (!hasSynced) {
            setupSyncFragment();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.filter) {
            showSearchFilterDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //just disable back button for now, figure out how it works later

//        DeviceListFragment deviceListFragment = (DeviceListFragment) getSupportFragmentManager().findFragmentByTag(DEVICE_LIST_FRAGMENT);
//        if (deviceListFragment != null && !deviceListFragment.onBackPressed()) {
//            super.onBackPressed();
//        }
//        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(HAS_SYNCED, hasSynced);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SyncingService.unregisterSync(this);
    }

    @Override
    public void onSyncFinish() {
        hasSynced = true;
        setupHomeFragment();
    }

    private void setupSyncFragment() {
        SyncFragment syncFragment = SyncFragment.newInstance();
        syncFragment.setOnSyncFinishListener(this);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container, syncFragment);
        fragmentTransaction.commit();
    }

    private void showSearchFilterDialog() {
        FragmentManager fm = getSupportFragmentManager();
        SearchFilterDialog searchFilterDialog = SearchFilterDialog.newInstance();
        searchFilterDialog.setOnApplyListener(getOnApplyListener());
        searchFilterDialog.show(fm, "search_filter_dialog");
    }

    private void setupHomeFragment() {
        homeFragment = HomeFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        fragmentTransaction.replace(R.id.container, homeFragment, HOME_FRAGMENT);
        fragmentTransaction.commit();
    }

    private SearchFilterDialog.OnFilterApplyListener getOnApplyListener() {
        return new SearchFilterDialog.OnFilterApplyListener() {
            @Override
            public void onApply() {
                //why is this returning null?!
                DeviceListFragment deviceListFragment = (DeviceListFragment) (getSupportFragmentManager().findFragmentByTag(DEVICE_LIST_FRAGMENT));
                deviceListFragment.refreshList();
            }
        };
    }


}
