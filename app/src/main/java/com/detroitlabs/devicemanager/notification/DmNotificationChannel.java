package com.detroitlabs.devicemanager.notification;


import static com.detroitlabs.devicemanager.constants.Constants.DEVICE_UPDATE_NOTIFICATION_CHANNEL_ID;

public interface DmNotificationChannel {
    String channelId();

    class UpdateDevice implements DmNotificationChannel {

        @Override
        public String channelId() {
            return DEVICE_UPDATE_NOTIFICATION_CHANNEL_ID;
        }
    }
}
