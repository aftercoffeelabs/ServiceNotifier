package com.example.aram.servicenotifier.notifier.model;

import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.example.aram.servicenotifier.R;
import com.example.aram.servicenotifier.notifier.service.SignalMonitorService;

/**
 * Class Notifier - Singleton
 */
public class Notifier {

    static final int NOTIFICATION_ID = 8792; // unique within app

    private static Notifier sInstance = null;

    private Context mContext = SignalMonitorService.getContext();
    private Resources mResources = SignalMonitorService.getContext().getResources();

    private NotificationManager mNotificationMgr;
    private NotificationCompat.Builder mBuilder;
    private Uri mSoundUri;

    /**
     * Class constructor.
     */
    private Notifier() {

        mNotificationMgr = (NotificationManager) mContext.getSystemService
                (Context.NOTIFICATION_SERVICE);

        // Define sound URI
        mSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // Initialize Notification Builder
        mBuilder = new NotificationCompat.Builder(mContext);
        setDefaultNotificationParameters();
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
    private void setDefaultNotificationParameters() {

        // These parameters will not change
        mBuilder.setSmallIcon(R.drawable.ic_launcher);
        mBuilder.setContentTitle(mResources.getString(R.string.notification_title));
        mBuilder.setSound(mSoundUri);

        //TODO: use different vibration pattern for in-service and out of service
        mBuilder.setVibrate(new long[]{0, 500, 500, 500});

        mBuilder.setShowWhen(true);
    }
}
