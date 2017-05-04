package com.detroitlabs.devicemanager;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.detroitlabs.devicemanager.databinding.ActivityMainBinding;
import com.detroitlabs.devicemanager.filter.SearchFilterDialog;
import com.detroitlabs.devicemanager.list.DeviceListFragment;
import com.detroitlabs.devicemanager.sync.SyncFragment;

public class MainActivity extends AppCompatActivity implements SyncFragment.OnSyncFinishListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String DEVICE_LIST_FRAGMENT = "DeviceListFragment";
    private ActivityMainBinding binding;
    private DeviceListFragment deviceListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setupToolbar();
        setupSyncFragment();
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
        } else if (id == R.id.account) {
            showAccount();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSyncFinish() {
        setupDeviceListView();
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

    private void setupDeviceListView() {
        deviceListFragment = DeviceListFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        fragmentTransaction.replace(R.id.container, deviceListFragment, DEVICE_LIST_FRAGMENT);
        fragmentTransaction.commit();
    }

    private void showAccount() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.GET_ACCOUNTS}, 0);
        }
        String email = "";
        for (Account account : AccountManager.get(this).getAccountsByType("com.google")) {
            email += account.name;
            email += "\n\n";
        }
        Log.d(TAG, "email: " + email);
    }

    private SearchFilterDialog.OnFilterApplyListener getOnApplyListener() {
        return new SearchFilterDialog.OnFilterApplyListener() {
            @Override
            public void onApply() {
                deviceListFragment.refreshList();
            }
        };
    }

    private void setupToolbar() {
        setSupportActionBar(binding.included1.toolbar);
    }
}
