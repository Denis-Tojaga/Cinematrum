package com.mob3000.cinematrum.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.mob3000.cinematrum.R;

public class NotificationReminderBroadcast extends BroadcastReceiver {

    private int ID_AUTOINCREMENT = 1;
    @Override
    public void onReceive(Context context, Intent intent) {
        //we are using the NotificationCompat.Builder to construct the notification details
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"MovieNotification")
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Don't forget your movie!")
                .setContentText("Movie starts in an hour, make sure you don't miss it.")
                .setPriority(NotificationCompat.PRIORITY_HIGH);



        //making the notificationManager
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);


        //in order to notify us we need to set the id to be unique for each notification
        notificationManager.notify(ID_AUTOINCREMENT,builder.build());
        ID_AUTOINCREMENT++;

    }
}
