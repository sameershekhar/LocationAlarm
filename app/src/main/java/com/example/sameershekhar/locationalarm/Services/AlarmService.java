package com.example.sameershekhar.locationalarm.Services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.sameershekhar.locationalarm.R;

/**
 * Created by sameershekhar on 17-Mar-18.
 */

public class AlarmService extends IntentService {
    private NotificationManager alarmNotificationManager;

    public AlarmService() {
        super("AlarmService");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        sendNotification("Wake Up! Wake Up!");
    }

    private void sendNotification(String msg) {
        Log.d("AlarmService", "Preparing to send notification...: " + msg);
        alarmNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

       // PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
         //       new Intent(this, ClockActivity.class), 0);

        NotificationCompat.Builder alamNotificationBuilder = new NotificationCompat.Builder(
                this).setContentTitle("Alarm").setSmallIcon(R.drawable.alarm)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg);


        //alamNotificationBuilder.setContentIntent(contentIntent);
        alarmNotificationManager.notify(1, alamNotificationBuilder.build());
        Log.d("AlarmService", "Notification sent.");
    }

}
