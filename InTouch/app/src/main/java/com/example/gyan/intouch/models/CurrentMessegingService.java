package com.example.gyan.intouch.models;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.gyan.intouch.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Gyan on 9/30/2017.
 */

public class CurrentMessegingService extends FirebaseMessagingService{

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String click_action = remoteMessage.getData().get("click_action");
        String senderId = remoteMessage.getData().get("from_sender_id").toString();
        String sender_name = remoteMessage.getData().get("sender_name").toString();
        String sender_status = remoteMessage.getData().get("sender_status").toString();
        String sender_image = remoteMessage.getData().get("sender_image").toString();


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle("New friend request")
                        .setSound(uri)
                        .setContentText(sender_name + " wants to be your friend");

        Intent resultIntent = new Intent(click_action);
        int reqID = (int) System.currentTimeMillis();
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        resultIntent.putExtra("userKey",senderId);
        resultIntent.putExtra("userName",sender_name);
        resultIntent.putExtra("userStatus",sender_status);
        resultIntent.putExtra("userThumb",sender_image);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, reqID, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        int mNotificationId = (int) System.currentTimeMillis();
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

}
