package com.detroitlabs.devicemanager.sync;


import android.content.Intent;

public interface Ui {
    void startActivityForResult(Intent intent, int requestCode, ActivityResult activityResult);

    interface ActivityResult {
        void onActivityResult(int resultCode, Intent data);
    }
}
