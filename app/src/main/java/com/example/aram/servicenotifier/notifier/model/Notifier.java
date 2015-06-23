package com.example.aram.servicenotifier.notifier.model;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.aram.servicenotifier.R;
import com.example.aram.servicenotifier.infrastructure.MyApp;

import java.util.Date;

/**
 * Class Notifier - Singleton
 */
public class Notifier {

    static final int NOTIFICATION_ID = 8792; // unique within app

    private static Notifier sInstance = null;

    private NotificationManager mNotificationMgr;
    private NotificationCompat.Builder mBuilder;
    private Uri mSoundUri;

    /**
     * Class constructor.
     */
    private Notifier() {

        mNotificationMgr = (NotificationManager) MyApp.getContext().getSystemService
                (Context.NOTIFICATION_SERVICE);

        // Define sound URI
        mSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // Init Notification Builder
        mBuilder = new NotificationCompat.Builder(MyApp.getContext());
        buildNotificationMessage();
    }

    /**
     * Returns the active instance of this class.
     */
    public static Notifier instance() {

        if(sInstance == null) {
            sInstance = new Notifier();
        }
        return sInstance;
    }

    public void setMessage(String message){
        mBuilder.setContentText(message);
        mBuilder.setWhen(System.currentTimeMillis());
    }

    /**
     * Sends message to notification bar
     */
    public void sendNotification() {
        mNotificationMgr.notify(NOTIFICATION_ID, mBuilder.build());
    }

    /**
     * Creates a notification bar message
     */
    private void buildNotificationMessage() {

        // These parameters will not change
        mBuilder.setSmallIcon(R.drawable.ic_launcher);
        mBuilder.setContentTitle("Phone Service Alert");
        mBuilder.setSound(mSoundUri);
        mBuilder.setVibrate(new long[] {0, 500, 500, 500});
        mBuilder.setShowWhen(true);

        /*
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("My Notification Title")
                        .setContentText("Something interesting happened");
        int NOTIFICATION_ID = 12345;

        Intent targetIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(NOTIFICATION_ID, builder.build());
        */
    }

    // TODO - remove this method
    public void playAudio(){
//        try {
//            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            Ringtone r = RingtoneManager.getRingtone(MyApp.getContext(), notification);
//            r.play();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        // TODO: use this as it includes sound when building the notification
//        //Define Notification Manager
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        //Define sound URI
//        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
//                .setSmallIcon(icon)
//                .setContentTitle(title)
//                .setContentText(message)
//                .setSound(soundUri); //This sets the sound to play
//
//        //Display notification
//        notificationManager.notify(0, mBuilder.build());
    }
}
