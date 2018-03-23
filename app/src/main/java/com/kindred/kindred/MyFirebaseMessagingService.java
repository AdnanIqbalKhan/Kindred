package com.kindred.kindred;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Objects;

import static android.content.ContentValues.TAG;

/**
 * Created by Adnan Iqbal Khan on 01-Mar-18.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private String mPostID;
    private static Map<Integer, String> NotifiList;
    NotificationManager mNotifyMgr;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_logo)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setSound(defaultSoundUri)
                .setAutoCancel(true);

        mPostID = remoteMessage.getData().get("post_id");
        Intent resultIntent = new Intent(remoteMessage.getNotification().getClickAction());
        resultIntent.putExtra("post_id", mPostID);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);

        int mNotificationId = (int) System.currentTimeMillis();
        mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

        NotifiList.put(mNotificationId, mPostID);

        FirebaseDatabase.getInstance().getReference().child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (Map.Entry<Integer, String> pair : NotifiList.entrySet()) {
                    String cState = dataSnapshot.child(pair.getValue()).child("confirmed").getValue().toString();
                    if (Objects.equals(cState, "true")) {
                        mNotifyMgr.cancel(pair.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
