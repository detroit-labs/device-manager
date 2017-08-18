package com.detroitlabs.devicemanager.ui;


import android.support.v7.util.DiffUtil;

import com.detroitlabs.devicemanager.db.Device;

import java.util.List;

public class DeviceDiffCallback extends DiffUtil.Callback {

    private final List<Device> oldList;
    private final List<Device> newList;

    public DeviceDiffCallback(List<Device> oldList, List<Device> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldPosition, int newPosition) {
        return oldList.get(oldPosition).serialNumber.equalsIgnoreCase(newList.get(newPosition).serialNumber);
    }

    @Override
    public boolean areContentsTheSame(int oldPosition, int newPosition) {
        Device oldDevice = oldList.get(oldPosition);
        Device newDevice = newList.get(newPosition);

        return oldDevice.equals(newDevice);
    }
}
