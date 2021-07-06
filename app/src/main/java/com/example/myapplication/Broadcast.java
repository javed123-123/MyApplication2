package com.example.myapplication;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static androidx.core.content.ContextCompat.getSystemService;


public class Broadcast extends BroadcastReceiver {
    Context ctx;
    @Override
    public void onReceive(Context context, Intent intent) {
        ctx=context;
        createNotificationChannel();
        NotificationManagerCompat myManager = NotificationManagerCompat.from(ctx);
        NotificationCompat.Builder myNoti = new NotificationCompat.Builder(ctx, "Channel_id");
        String task=intent.getStringExtra("noti");
        myNoti.setContentTitle(task);
        myNoti.setContentText("Deadline of this task is completed!");
        myNoti.setSmallIcon(android.R.drawable.star_big_on);
//CHECK WHAT HAPPENS!
        Intent i1 = new Intent(ctx, MainActivity.class);
        PendingIntent pd = PendingIntent.getActivity(ctx,1,i1,0);
        myNoti.setContentIntent(pd);
        myNoti.setAutoCancel(true);
        myManager.notify(1,myNoti.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Channel_id","name", importance);
            channel.setDescription("description");
            NotificationManager notificationManager = getSystemService(ctx,NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
