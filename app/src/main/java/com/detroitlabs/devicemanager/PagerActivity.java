package com.detroitlabs.devicemanager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.crashlytics.android.Crashlytics;
import com.detroitlabs.devicemanager.ui.BackPressListener;
import com.detroitlabs.devicemanager.ui.DeviceListFragment;
import com.detroitlabs.devicemanager.ui.HomeFragment;

import io.fabric.sdk.android.Fabric;

public class PagerActivity extends AppCompatActivity {

    private DevicePagerAdapter pagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        setupViewPager();
    }

    @Override
    public void onBackPressed() {
        int currentPagerIndex = viewPager.getCurrentItem();
        boolean backPressHandled = pagerAdapter.onBackPressed(currentPagerIndex);
        if (!backPressHandled) {
            super.onBackPressed();
        }
    }

    private void setupViewPager() {
        pagerAdapter = new DevicePagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
    }

    private class DevicePagerAdapter extends FragmentStatePagerAdapter {

        SparseArray<Fragment> registeredFragments = new SparseArray<>();

        DevicePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (i == 0) {
                HomeFragment frag = HomeFragment.newInstance();
                registeredFragments.append(i, frag);
                return frag;
            } else if (i == 1) {
                DeviceListFragment frag = DeviceListFragment.newInstance();
                registeredFragments.append(i, frag);
                return frag;
            } else {
                return null;
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        @Override
        public int getCount() {
            return 2;
        }


        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }

        public boolean onBackPressed(int currentPagerIndex) {
            Fragment registeredFragment = getRegisteredFragment(currentPagerIndex);
            return ((BackPressListener) registeredFragment).onBackPressed();
        }
    }
}
