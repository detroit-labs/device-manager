package com.detroitlabs.devicemanager.list;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;

import com.detroitlabs.devicemanager.R;
import com.detroitlabs.devicemanager.data.DeviceUpdateService;
import com.detroitlabs.devicemanager.utils.DeviceUtil;


public class CheckInOutDialogFragment extends DialogFragment {

    @StringRes
    private int titleRes;

    public static CheckInOutDialogFragment newInstance() {

        Bundle args = new Bundle();

        CheckInOutDialogFragment fragment = new CheckInOutDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        initData();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(titleRes);
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onConfirmClicked();
            }
        });
        return builder.create();
    }

    private void onConfirmClicked() {
        if (DeviceUtil.isCheckedOut()) {
            DeviceUpdateService.checkInDevice(getContext());
        } else {
            DeviceUpdateService.checkOutDevice(getContext(), "Nick");
        }
    }

    private void initData() {
        if (DeviceUtil.isCheckedOut()) {
            titleRes = R.string.check_in;
        } else {
            titleRes = R.string.check_out;
        }
    }
}
