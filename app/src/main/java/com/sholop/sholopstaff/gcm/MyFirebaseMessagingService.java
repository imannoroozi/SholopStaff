package com.sholop.sholopstaff.gcm;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sholop.sholopstaff.objects.Notification;

/**
 * Created by Pooyan on 1/21/2017.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        Log.d("Notif", "From: " + remoteMessage.getFrom());
        Log.d("Notif", "Notification Message Body: " + remoteMessage.getNotification().getBody());

        Notification notification = new Notification(bundle);


        // verifying whether the app is in background or foreground
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            //ring bell
            updateActivity(getApplicationContext(), "Test message.");
//            showNotificationMessage(notification);
        } else {
            showNotificationMessage(notification);
        }

//        sendNotification(remoteMessage.getNotification().getBody());
    }
}
