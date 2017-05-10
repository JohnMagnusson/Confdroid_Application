package com.bykth.confdroid.confdroid_application;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import java.util.Random;

/**
 * Created by Glantz on 2017-05-08.
 */
public class NotificationActivity extends Activity {

    public static final String NOTIFICATION_ID = "NOTIFICATION_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(getIntent().getIntExtra(NOTIFICATION_ID, -1));
        finish(); // since finish() is called in onCreate(), onDestroy() will be called immediately
    }

    private static PendingIntent getDismissIntent(int notificationId, Context context) {
        Intent intent = new Intent(context, NotificationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(NOTIFICATION_ID, notificationId);
        PendingIntent dismissIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        return dismissIntent;
    }

    private static PendingIntent getDownloadIntent(int notificationId, Context context) {
        Intent intent = new Intent(context, ActionReceiver.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("action", "action1");
        intent.putExtra(NOTIFICATION_ID, notificationId);
        PendingIntent downloadIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        return downloadIntent;
    }

    public void showNotification(Context context) {

        int notificationId = new Random().nextInt(); // just use a counter in some util class...
        PendingIntent dismissIntent = NotificationActivity.getDismissIntent(notificationId, context);
        PendingIntent downloadIntent = NotificationActivity.getDownloadIntent(notificationId, context);


        NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setVibrate(new long[]{250, 250, 300, 300, 250, 250})
                .setContentTitle("Update")
                .setContentText("New updates are here" + "\n" + " Do you want to Install now or later?")
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(downloadIntent)
                .addAction(android.R.drawable.ic_menu_save, "Now", downloadIntent)
                .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Later", dismissIntent)
                .setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, notification.build());
    }


}