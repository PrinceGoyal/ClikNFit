package com.cliknfit.fcm;

/**
 * Created by Prince on 9/14/2016.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.cliknfit.R;
import com.cliknfit.activity.Dashboard;
import com.cliknfit.util.AppPreference;
import com.cliknfit.util.Constants;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Log.d("FCM", "From: " + remoteMessage.getFrom());
        String jsonStr = remoteMessage.getNotification().getBody();
        String json = remoteMessage.getData().toString();
        if (AppPreference.getPreference(this, Constants.MUTE).equals("")) {
            sendNotification(remoteMessage.getNotification().getTitle(), jsonStr);
            Intent broadcastIntent = new Intent();
            broadcastIntent.putExtra("model", json);
            broadcastIntent.setAction("com.cliknfit");
            sendBroadcast(broadcastIntent);
        }
    }

    private void parsejson(String jsonStr) {
        try {
            JSONObject mainobj = new JSONObject(jsonStr);
            JSONObject notiJson = mainobj.getJSONObject("notification");
            if (notiJson.getInt("code") == 1) {
               // sendNotification(notiJson.getString("message"));
            } else {
                Intent broadcastIntent = new Intent();
                broadcastIntent.putExtra("name", notiJson.getString("name"));
                broadcastIntent.putExtra("mobile", notiJson.getString("mobile"));
                broadcastIntent.putExtra("picture", notiJson.getString("picture"));
                broadcastIntent.putExtra("status", notiJson.getString("status"));
                broadcastIntent.setAction("com.lawkey.emergency");
                sendBroadcast(broadcastIntent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendNotification(String title, String messageBody) {
        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        //Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.notiicon);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.launcher)
                //.setLargeIcon(largeIcon)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(messageBody))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Random r = new Random();
        int rndm = r.nextInt(1000);

        notificationManager.notify(rndm, notificationBuilder.build());
    }
}