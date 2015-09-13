package com.example.aram.servicenotifier.notifier.model;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.aram.servicenotifier.R;
import com.example.aram.servicenotifier.infrastructure.MyApp;
import com.example.aram.servicenotifier.notifier.service.SignalMonitorService;

/**
 * Class Notifier - Singleton
 */
public class Notifier {

    public static final int NOTIFICATION_ID = 8792; // unique within app
    public static final int VIBRATE_TWO_SHORT = 1;
    public static final int VIBRATE_ONE_LONG = 2;

    private static Notifier sInstance = null;

    private Context mContext = SignalMonitorService.getContext();
    private Resources mResources = SignalMonitorService.getContext().getResources();

    private NotificationManager mNotificationMgr;
    private NotificationCompat.Builder mBuilder;
    private Uri mSoundUri;

    private static boolean doVibrate = true;

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

        readSharedPref();
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

    public void setVibratePattern(int pattern) {

        if (doVibrate) {
            // Vibrate spec: { start delay, vibrate, sleep, ...}
            switch (pattern) {
                case VIBRATE_TWO_SHORT:
                    mBuilder.setVibrate(new long[]{0, 500, 500, 500});
                    break;
                case VIBRATE_ONE_LONG:
                    mBuilder.setVibrate(new long[]{0, 2000});
                    break;
            }
        } else {
            // User has disabled vibration in settings
            mBuilder.setVibrate(new long[]{0l});
        }
    }

    public void setMessageIconColor(int argb) {

        // setColor only supported on Lollipop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBuilder.setColor(argb);
        }
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
        mBuilder.setSmallIcon(R.drawable.ic_notification_bars);
        mBuilder.setContentTitle(mResources.getString(R.string.notification_title));
        mBuilder.setSound(mSoundUri);
        mBuilder.setCategory(Notification.CATEGORY_STATUS);
        mBuilder.setShowWhen(true);
    }

    /**
     * Static method to set the vibrate feature based on user settings
     */
    public static void doVibrate(boolean state) {

        doVibrate = state;
    }

    /**
     * Loads user settings upon initialization
     */
    private void readSharedPref() {

        String key = MyApp.getRes().getString(R.string.sharedPrefKey_vibrate);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApp.getContext());
        doVibrate = prefs.getBoolean(key, true);
    }
}
