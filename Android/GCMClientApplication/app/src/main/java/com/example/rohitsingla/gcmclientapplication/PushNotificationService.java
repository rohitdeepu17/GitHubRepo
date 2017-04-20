package com.example.rohitsingla.gcmclientapplication;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by rohitsingla on 20/05/16.
 */
public class PushNotificationService extends GcmListenerService {
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        //createNotification(mTitle, push_msg);
    }
}