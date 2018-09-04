package com.prv.gootoor.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;
import com.prv.gootoor.R;

import com.prv.gootoor.activities.LoginActivity;
import com.prv.gootoor.utils.Config;

import java.util.Random;

/**
 * Created by roger on 11/11/2017.
 */

public class MyGcmListenerService extends GcmListenerService {

    private Config mConfig = Config.sharedInstance();
    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */

    @Override
    public void onMessageReceived(String from, Bundle data) {

        String message = data.getString("message");
        String title = data.getString("title");
        String vibrate = data.getString("vibrate");
        String sound = data.getString("sound");
        String largeIcon = data.getString("largeIcon");
        String smallIcon = data.getString("smallIcon");

        String[] valeurArray = message.split("_");
        String valeurStr = valeurArray[valeurArray.length-1];
        int badge = Integer.parseInt(valeurStr);
        valeurStr = valeurArray[valeurArray.length-2];
        int product_id = Integer.parseInt(valeurStr);

        SharedPreferences sharedPref = getSharedPreferences(mConfig.getFileParameters(), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(mConfig.getDomaineApp()+"product_id", product_id);
        editor.putInt(mConfig.getDomaineApp()+"badge", badge);
        editor.apply();


        sendNotification(message, title);
    }

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message, String title) {


        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder)
                new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);

        // On génère un nombre aléatoire pour pouvoir afficher plusieurs notifications
        notificationManager.notify(new Random().nextInt(9999), notificationBuilder.build());
    }
}