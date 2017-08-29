package com.detroitlabs.devicemanager.ui;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.transition.TransitionManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.detroitlabs.devicemanager.databinding.FragHomeBinding;
import com.detroitlabs.devicemanager.db.Device;
import com.detroitlabs.devicemanager.utils.ViewUtil;


public class HomeFragment extends LifecycleFragment {

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
                if (device != null) {
                    animVisibility(device);
                }
            }
        });
    }

    private void animVisibility(Device device) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(binding.transitionContainer);
        }

        setVisible(binding.checkoutArea, !device.notRegisterable && !device.isCheckedOut());
        setVisible(binding.status.textNotRegisterable, device.notRegisterable);
        setVisible(binding.status.viewAvailable, !device.notRegisterable && !device.hasRequest() && !device.isCheckedOut());
        setVisible(binding.status.viewRequest, device.hasRequest());
        setVisible(binding.status.viewCheckout, device.isCheckedOut());
    }

    private void setVisible(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void setupView() {
        binding.buttonCheckout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ViewUtil.hideKeyboard(view);
                binding.editTextUsername.clearFocus();
                return false;
            }
        });
        binding.editTextUsername.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    viewModel.checkOut();
                }
                return false;
            }
        });
        binding.buttonOtherDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 7/24/17 swipe to list page
            }
        });
    }
}
