package com.detroitlabs.devicemanager.ui;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.detroitlabs.devicemanager.R;
import com.detroitlabs.devicemanager.databinding.FragHomeBinding;
import com.detroitlabs.devicemanager.db.Device;


public class HomeFragment extends LifecycleFragment {

    private static final String DEVICE_LIST_FRAGMENT = "DeviceListFragment";
    private FragHomeBinding binding;
    private HomeViewModel viewModel;


    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragHomeBinding.inflate(inflater, container, false);
        setupView();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        binding.setViewModel(viewModel);
        // TODO: 7/13/17 clean up
        viewModel.getSelf().observe(this, new Observer<Device>() {
            @Override
            public void onChanged(@Nullable Device device) {
                binding.setDevice(device);
            }
        });
    }

    private void setupView() {
//        binding.buttonOtherDevices.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DeviceListFragment deviceListFragment = DeviceListFragment.newInstance();
//                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.container, deviceListFragment, DEVICE_LIST_FRAGMENT);
//                fragmentTransaction.commit();
//            }
//        });
    }
}
