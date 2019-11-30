package com.thinkty.toolkit;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    private static Ringtone ringtone;
    private NotificationManager notificationManager;
    private final String CHANNEL_ID = "TOOLKIT_CHANNEL_ID";
    private final int NOTIFICATION_ID = 69; // No specific reason

    public static Ringtone getRingtone() {
        return ringtone;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmReceiver", "Broadcast received");

        // Get the alarm ringtone uri
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        // Play the ringtone
        ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();

        Log.d("AlarmService", "Preparing to send alarm notification");
        notificationManager = context.getSystemService(NotificationManager.class);

        notificationManager.createNotificationChannel(new NotificationChannel(CHANNEL_ID, "TOOLKIT_CHANNEL", NotificationManager.IMPORTANCE_DEFAULT));

        // Intent on click of notification
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, AlarmActivity.class), 0);

        // Make notification format
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.alarm_br_icon)
                .setContentTitle("Alarm")
                .setContentText("Wake up!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        notificationManager.notify(NOTIFICATION_ID, builder.build());

        // Must send the result in 10 seconds or app is killed :(
        setResultCode(Activity.RESULT_OK);
    }
}
