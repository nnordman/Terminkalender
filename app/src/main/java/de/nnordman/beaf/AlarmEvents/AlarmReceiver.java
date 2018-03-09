package de.nnordman.beaf.AlarmEvents;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import de.nnordman.beaf.EventUebersicht;

import static de.nnordman.beaf.AlarmEvents.NotificationScheduler.TAG;


public class AlarmReceiver extends BroadcastReceiver{


    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                // Set the alarm here.
                Log.d(TAG, "onReceive: BOOT_COMPLETED");
                NotificationScheduler.setReminder(context, AlarmReceiver.class, 10, 00, 2018, 0, 29);
                return;
            }
        }
        //Trigger the notification
        NotificationScheduler.showNotification(context, EventUebersicht.class,
                "You have 5 unwatched videos", "Watch them now?");

        NotificationScheduler.cancelReminder(context, AlarmReceiver.class);
    }

}