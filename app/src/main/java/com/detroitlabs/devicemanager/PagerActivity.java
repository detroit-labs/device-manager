package com.detroitlabs.devicemanager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;
import com.detroitlabs.devicemanager.sync.SyncingService;
import com.detroitlabs.devicemanager.ui.DeviceListFragment;
import com.detroitlabs.devicemanager.ui.HomeFragment;

import io.fabric.sdk.android.Fabric;

public class PagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        setupViewPager();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SyncingService.unregisterSync(this);
    }

    private void setupViewPager() {
        DevicePagerAdapter pagerAdapter = new DevicePagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
    }

    private class DevicePagerAdapter extends FragmentStatePagerAdapter {

        DevicePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (i == 0) {
                return HomeFragment.newInstance();
            } else if (i == 1) {
                return DeviceListFragment.newInstance();
            } else {
                return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
