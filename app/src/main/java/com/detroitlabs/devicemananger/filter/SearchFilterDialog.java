package com.detroitlabs.devicemananger.filter;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.detroitlabs.devicemananger.R;


public class SearchFilterDialog extends DialogFragment {
    public static SearchFilterDialog newInstance() {

        Bundle args = new Bundle();

        SearchFilterDialog fragment = new SearchFilterDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(new SearchFilterView(getActivity()));
        builder.setTitle("Search Filter");
        builder.setPositiveButton(R.string.apply, null);
        builder.setNegativeButton(R.string.cancel, null);
        return builder.create();
    }
}
