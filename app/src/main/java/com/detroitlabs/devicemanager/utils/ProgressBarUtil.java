package com.detroitlabs.devicemanager.utils;


import android.animation.ObjectAnimator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;

public class ProgressBarUtil {
    public static void animateToPercent(ProgressBar progressBar, int percent) {
        ObjectAnimator animator = ObjectAnimator.ofInt(progressBar, "progress", percent);
        animator.setDuration(1000);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }
}
