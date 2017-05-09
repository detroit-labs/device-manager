package com.detroitlabs.devicemanager.list;


import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.detroitlabs.devicemanager.R;
import com.detroitlabs.devicemanager.databinding.ViewDeviceListItemBinding;
import com.detroitlabs.devicemanager.utils.DeviceUtil;

import static com.detroitlabs.devicemanager.data.DatabaseContract.DEVICE_URI;

public class ThisDeviceFragment extends Fragment {
    private static final String TAG = ThisDeviceFragment.class.getSimpleName();
    private ViewDeviceListItemBinding binding;
    private ThisDeviceContentObserver contentObserver;
    private OnItemClickListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentObserver = new ThisDeviceContentObserver();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ViewDeviceListItemBinding.inflate(inflater, container, false);
        initView();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        getContext().getContentResolver().registerContentObserver(
                DEVICE_URI,
                true,
                contentObserver
        );
    }

    @Override
    public void onStop() {
        super.onStop();
        getContext().getContentResolver().unregisterContentObserver(contentObserver);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private void initView() {
        binding.status.setImageResource(R.drawable.ic_smartphone_white_24dp);
        binding.status.setColorFilter(getColor(R.color.grey));
        binding.deviceItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(DeviceUtil.getDevice());
                }
            }
        });
        setupData();
    }

    private int getColor(@ColorRes int colorRes) {
        return ContextCompat.getColor(getContext(), colorRes);
    }

    private void onCheckOutStatusChanged() {
        setupData();
    }

    private void setupData() {
        binding.setDetail(DeviceUtil.getDevice());
        setStatus(DeviceUtil.isCheckedOut());
        binding.executePendingBindings();
    }

    private void setStatus(boolean isCheckedOut) {
        if (!isCheckedOut) {
            binding.status.setColorFilter(getColor(R.color.green));
        } else {
            binding.status.setColorFilter(getColor(R.color.grey));
        }
    }

    private final class ThisDeviceContentObserver extends ContentObserver {

        ThisDeviceContentObserver() {
            super(new Handler(Looper.getMainLooper()));
        }

        @Override
        public boolean deliverSelfNotifications() {
            return true;
        }

        @Override
        public void onChange(boolean selfChange) {
            Log.d(TAG, "THIS DEVICE FRAGMENT RECEIVE CHANGE");
            onCheckOutStatusChanged();
        }

    }
}
