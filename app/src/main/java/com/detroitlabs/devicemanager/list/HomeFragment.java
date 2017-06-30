package com.detroitlabs.devicemanager.list;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.detroitlabs.devicemanager.R;
import com.detroitlabs.devicemanager.data.DeviceUpdateService;
import com.detroitlabs.devicemanager.databinding.FragHomeBinding;
import com.detroitlabs.devicemanager.models.Device;
import com.detroitlabs.devicemanager.utils.DeviceUtil;


public class HomeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Device> {

    private static final int LOADER_ID = 334;
    private static final String DEVICE_LIST_FRAGMENT = "DeviceListFragment";
    private Device thisDevice;
    private FragHomeBinding binding;


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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragHomeBinding.inflate(inflater, container, false);
        setupView();
        return binding.getRoot();
    }

    private void setupView() {
        //these colors should always be set regardless if the icons are visible or not
        binding.iconAvailable.setColorFilter(getColor(R.color.green));
        binding.iconCheckedOutBy.setColorFilter(getColor(R.color.red));
        binding.iconRequestedBy.setColorFilter(getColor(R.color.orange));

        binding.buttonOtherDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceListFragment deviceListFragment = DeviceListFragment.newInstance();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, deviceListFragment, DEVICE_LIST_FRAGMENT);
                fragmentTransaction.commit();
            }
        });

        // "release" == "check in"
        binding.buttonRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceUpdateService.checkInDevice(getContext());
            }
        });
        binding.buttonCancelRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceUpdateService.requestDevice(getContext(), thisDevice.serialNumber, "");
            }
        });
        binding.buttonCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = binding.editTextUsername;
                String textValue = editText.getText().toString();
                if (textValue == "") return;
                DeviceUpdateService.checkOutDevice(getContext(), textValue);
                editText.setText("");
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    private int getColor(@ColorRes int colorRes) {
        return ContextCompat.getColor(getContext(), colorRes);
    }

    @Override
    public Loader<Device> onCreateLoader(int id, Bundle args) {
        return new ThisDeviceTaskLoader(getContext());
    }

    @Override
    public void onLoadFinished(Loader<Device> loader, Device data) {
        boolean registerable = true;
        if (data == null){
            data = DeviceUtil.readThisDevice(getContext());
            registerable = false;
        }
        setupData(data);
        adjustInputFrameControls(registerable);
    }

    private void adjustInputFrameControls(boolean registerable) {
        if (registerable){
            registerableInputControls();
        } else {
            notRegisterable();
        }
    }

    private void notRegisterable() {
        binding.textNoMoreRequests.setVisibility(View.GONE);
        binding.editTextUsername.setVisibility(View.GONE);
        binding.buttonRequest.setVisibility(View.GONE);
        binding.buttonCheckout.setVisibility(View.GONE);
        binding.buttonRegister.setVisibility(View.GONE);
        binding.iconAvailable.setVisibility(View.GONE);
        binding.statusAvailable.getChildAt(1).setVisibility(View.GONE);
        binding.statusAvailable.getChildAt(2).setVisibility(View.VISIBLE);
    }

    private void registerableInputControls(){
        if (thisDevice.hasRequest() && thisDevice.isCheckedOut()) { //checked out and requested, user can't do anything
            binding.textNoMoreRequests.setVisibility(View.VISIBLE);
            binding.editTextUsername.setVisibility(View.GONE);
            binding.buttonRequest.setVisibility(View.GONE);
            binding.buttonCheckout.setVisibility(View.GONE);
            binding.buttonRegister.setVisibility(View.GONE);//todo might eventually nix register button
        } else if (!thisDevice.hasRequest() && thisDevice.isCheckedOut()) { //checked out but not requested, user can't do anything
            binding.textNoMoreRequests.setVisibility(View.VISIBLE);
            binding.editTextUsername.setVisibility(View.GONE);
            binding.buttonRequest.setVisibility(View.GONE);
            binding.buttonCheckout.setVisibility(View.GONE);
            binding.buttonRegister.setVisibility(View.GONE);
        } else if (!thisDevice.hasRequest() && !thisDevice.isCheckedOut()) { //neither checked out or requested, user can check out
            binding.textNoMoreRequests.setVisibility(View.GONE);
            binding.editTextUsername.setVisibility(View.VISIBLE);
            binding.buttonRequest.setVisibility(View.GONE);
            binding.buttonCheckout.setVisibility(View.VISIBLE);
            binding.buttonRegister.setVisibility(View.GONE);
        } else if (thisDevice.hasRequest() && !thisDevice.isCheckedOut()) { //requested but no checked out, user can check out
            binding.textNoMoreRequests.setVisibility(View.GONE);
            binding.editTextUsername.setVisibility(View.VISIBLE);
            binding.buttonRequest.setVisibility(View.GONE);
            binding.buttonCheckout.setVisibility(View.VISIBLE);
            binding.buttonRegister.setVisibility(View.GONE);
        }
    }

    private void setupData(Device device) {
        this.thisDevice = device;
        binding.setDetail(thisDevice);
        binding.executePendingBindings();
    }


    @Override
    public void onLoaderReset(Loader<Device> loader) {

    }


}
