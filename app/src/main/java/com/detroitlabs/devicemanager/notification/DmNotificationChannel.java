package com.detroitlabs.devicemanager.notification;


import static com.detroitlabs.devicemanager.constants.Constants.NOTIFICATION_CHANNEL_UPDATE_DEVICE_ID;
import static com.detroitlabs.devicemanager.constants.Constants.NOTIFICATION_CHANNEL_UPDATE_DEVICE_NAME;

public interface DmNotificationChannel {
    String channelId();

    String channelName();

    class UpdateDevice implements DmNotificationChannel {

        @Override
        public String channelId() {
            return NOTIFICATION_CHANNEL_UPDATE_DEVICE_ID;
        }

        @Override
        public String channelName() {
            return NOTIFICATION_CHANNEL_UPDATE_DEVICE_NAME;
        }
    }
}
