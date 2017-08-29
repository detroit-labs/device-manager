package com.detroitlabs.devicemanager.utils;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

public class ViewUtil {
    public static void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void toggleView(@NonNull View view) {
        view.setVisibility(view.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }

    public static void toggleViewAnimated(@NonNull ViewGroup sceneRoot, @NonNull View... views) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(sceneRoot);
        }
        for (View view : views) {
            toggleView(view);
        }
    }
}
